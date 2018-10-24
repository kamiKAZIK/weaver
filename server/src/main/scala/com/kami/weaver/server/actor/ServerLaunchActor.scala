package com.kami.weaver.server.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.stream.Materializer
import com.kami.weaver.server.util.InterfaceBinding

object ServerLaunchActor {
  case object Start
  case class Bind(address: String)

  def props(schemaManagerActor: ActorRef)(implicit materializer: Materializer): Props =
    Props(new ServerLaunchActor(schemaManagerActor))
}

class ServerLaunchActor(schemaManagerActor: ActorRef)(implicit materializer: Materializer) extends Actor
  with ActorLogging {

  import context.{dispatcher, system}

  override def receive: Receive = {
    case ServerLaunchActor.Start =>
      schemaManagerActor ! SchemaManagerActor.SetUp
    case ServerLaunchActor.Bind(address) =>
      InterfaceBinding.bind(address)
        .map(binding => log.info("Server started successfully on {}", binding.localAddress))
        .recover {
          case e: Throwable =>
            log.error(e, "Failed to start server!")
            system.terminate()
        }
    case SchemaManagerActor.SetUpCompleted =>
      self ! ServerLaunchActor.Bind(context.system.settings.config.getString("api-server.binding.address"))
    case SchemaManagerActor.SetUpFailed(e) =>
      log.error(e, "Failed to initialize database schema!")
    case _ =>
      log.error("Unknown message received!")
  }
}
