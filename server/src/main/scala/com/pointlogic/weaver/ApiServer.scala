package com.pointlogic.weaver

import java.nio.file.Paths

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import com.pointlogic.weaver.data.SchemaInitializer
import com.pointlogic.weaver.route.{BinaryRoute, SparkSessionRoute}
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ApiServer {
  private val Logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    launch(args, config => ActorSystem("ApiServer", config))
  }

  private def launch(args: Array[String], actorSystem: Config => ActorSystem): Unit = {
    val defaultConfig = ConfigFactory.load()
    val config = if (args.length > 0) {
      ConfigFactory.parseFile(Paths.get(args(0)).toFile)
        .withFallback(defaultConfig)
        .resolve()
    } else {
      defaultConfig.resolve()
    }

    implicit val system: ActorSystem = actorSystem(config)
    implicit val materializer: Materializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = system.dispatcher

    SchemaInitializer.init

    val routes = new BinaryRoute().routes ~ new SparkSessionRoute().routes

    bind(config, routes)
  }

  private def bind(config: Config, routes: Route)(implicit actorSystem: ActorSystem, materializer: Materializer, executionContext: ExecutionContext): Unit = {
    val address = config.getString("api-server.binding.address")
    val port = config.getInt("api-server.binding.port")

    val bindingFuture = Http().bindAndHandle(routes, address, port)
    Logger.info(s"Server online at http://$address:$port...")

    CoordinatedShutdown(actorSystem).addTask(
      CoordinatedShutdown.PhaseServiceUnbind, "http_shutdown") { () =>

      bindingFuture.flatMap(_.terminate(hardDeadline = 15.seconds)).map { _ =>
        Logger.info(s"Server shutdown completed...")
        Done
      }
    }
  }
}
