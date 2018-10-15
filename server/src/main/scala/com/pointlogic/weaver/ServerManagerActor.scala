package com.pointlogic.weaver

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.pointlogic.weaver.data.SchemaManagerActor

import scala.concurrent.Future

object ServerManagerActor {
  case object Start
  case class Bind(address: String)

  def props(schemaManagerActor: ActorRef)(implicit materializer: Materializer): Props =
    Props(new ServerManagerActor(schemaManagerActor))

  def http(route: Route, address: String)(implicit system: ActorSystem, materializer: Materializer): Future[Http.ServerBinding] =
    Http().bindAndHandle(route, address)
}

class ServerManagerActor(schemaManagerActor: ActorRef)(implicit materializer: Materializer) extends Actor
  with ActorLogging {

  import context.{system, dispatcher}

  override def receive: Receive = {
    case ServerManagerActor.Start =>
      schemaManagerActor ! SchemaManagerActor.Initialize
    case ServerManagerActor.Bind(address) =>
      bind(address)
    case SchemaManagerActor.Initialized =>
      self ! ServerManagerActor.Bind(context.system.settings.config.getString("api-server.binding.address"))
    case SchemaManagerActor.InitializationError(e) =>
      log.error(e, "Failed to initialize database schema!")
    case _ =>
      log.error("Unknown message received!")
  }

  private def bind(address: String): Future[Unit] = {
    val routes = path("a") {
      complete("a")
    } ~ path("b") {
      complete("b")
    }

    ServerManagerActor.http(routes, address)
      .map(binding => log.info("Server started successfully on {}", binding.localAddress))
      .recover {
        case e: Throwable =>
          log.error(e, "Failed to start server!")
          system.terminate()
      }
  }
}
