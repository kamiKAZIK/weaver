package com.pointlogic.weaver

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.pointlogic.weaver.data.SchemaManagerActor
import com.typesafe.config.{Config, ConfigFactory}

object ApiServer {
  def main(args: Array[String]): Unit = {
    launch(args, config => ActorSystem("ApiServer", config))
  }

  private def launch(args: Array[String], system: Config => ActorSystem): Unit = {
    val defaultConfig = ConfigFactory.load()
    val config = if (args.length > 0) {
      ConfigFactory.parseFile(Paths.get(args(0)).toFile)
        .withFallback(defaultConfig)
        .resolve()
    } else {
      defaultConfig.resolve()
    }

    implicit val actorSystem: ActorSystem = system(config)
    implicit val materializer: Materializer = ActorMaterializer()

    actorSystem
      .actorOf(ServerManagerActor.props(actorSystem.actorOf(SchemaManagerActor.props))) ! ServerManagerActor.Start
  }
}
