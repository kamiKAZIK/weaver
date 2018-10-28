package com.kami.weaver.server.actor

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.pipe
import akka.stream.Materializer
import com.kami.weaver.server.util.InterfaceBinding

object ServerLaunchActor {
  case object Start
  case class Bind(address: String)
  case class BindingSucceeded(address: InetSocketAddress)
  case class BindingFailed(e: Throwable)

  def props(schemaManagerActor: ActorRef, routeManagerActor: ActorRef)(implicit materializer: Materializer): Props =
    Props(new ServerLaunchActor(schemaManagerActor, routeManagerActor))
}

class ServerLaunchActor(schemaManagerActor: ActorRef, routeManagerActor: ActorRef)(implicit materializer: Materializer) extends Actor
  with ActorLogging {

  import context.{dispatcher, system}

  override def receive: Receive = {
    case ServerLaunchActor.Start =>
      routeManagerActor ! RouteManagerActor.SetUp
    case ServerLaunchActor.Bind(address) =>
      InterfaceBinding.bind(address)
        .map(binding => ServerLaunchActor.BindingSucceeded(binding.localAddress))
        .recover {
          case e: Throwable =>
            ServerLaunchActor.BindingFailed(e)
        } pipeTo sender
    case ServerLaunchActor.BindingSucceeded(address) =>
      log.info("Server started successfully on {}", address)
    case RouteManagerActor.SetUpCompleted =>
      schemaManagerActor ! SchemaManagerActor.SetUp
    case SchemaManagerActor.SetUpCompleted =>
      self ! ServerLaunchActor.Bind(context.system.settings.config.getString("api-server.binding.address"))
    case ServerLaunchActor.BindingFailed(e) =>
      log.error(e, "Failed to start the server!")
      system.terminate
    case RouteManagerActor.SetUpFailed(e) =>
      log.error(e, "Failed to initialize dynamic routes!")
      system.terminate
    case SchemaManagerActor.SetUpFailed(e) =>
      log.error(e, "Failed to initialize database schema!")
      system.terminate
    case _ =>
      log.error("Unknown message received!")
  }
}
