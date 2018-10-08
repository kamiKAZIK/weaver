package com.pointlogic.weaver

import java.io.File
import java.nio.file.{Files, Paths}

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory
import slick.basic.DatabaseConfig
import slick.jdbc.H2Profile

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ApiServer {
  private val Logger = LoggerFactory.getLogger(getClass)

  //implicit val materializer = ActorMaterializer()

  //val routes = get {
  //  complete("Hello world!")
  //}

  def main(args: Array[String]): Unit = {
    launch(args, config => ActorSystem("ApiServer", config))
  }

  private def launch(args: Array[String], actorSystem: Config => ActorSystem): Unit = {
    val defaultConfig = ConfigFactory.load()
    val config = if (args.length > 0) {
      val configPath = Paths.get(args(0))
      if (!Files.exists(configPath)) {
        Logger.error("Could not find configuration file {}", configPath)
        sys.exit(1)
      }
      ConfigFactory.parseFile(configPath.toFile)
        .withFallback(defaultConfig)
        .resolve()
    } else {
      defaultConfig.resolve()
    }

    implicit val system: ActorSystem = actorSystem(config)
    bind(config, null)(system, system.dispatcher, ActorMaterializer())
  }

  private def bind(config: Config, routes: Route)(implicit actorSystem: ActorSystem, executionContext: ExecutionContext, materializer: Materializer): Unit = {
    val address = config.getString("api-server.binding.address")
    val port = config.getInt("api-server.binding.port")

    val bindingFuture = Http().bindAndHandle(routes, address, port)
    println(s"Server online at http://$address:$port...")

    CoordinatedShutdown(actorSystem).addTask(
      CoordinatedShutdown.PhaseServiceUnbind, "http_shutdown") { () =>

      bindingFuture.flatMap(_.terminate(hardDeadline = 15.seconds)).map { _ =>
        println(s"Server shutdown completed...")
        Done
      }
    }
  }
}
