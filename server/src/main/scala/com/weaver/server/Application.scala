package com.weaver.server

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.Config
import com.weaver.server.actor.{RouteManagerActor, SchemaManagerActor, ServerLaunchActor}
import com.weaver.server.configuration.ConfigurationProvider

import scala.util.Try

object Application extends ConfigurationProvider {
  def main(args: Array[String]): Unit = {
    launch(args, config => ActorSystem("ApiServer", config))
  }

  private def launch(args: Array[String], system: Config => ActorSystem): Unit = {
    implicit val actorSystem: ActorSystem = system(resolve(Try(args(0)).toOption))
    implicit val materializer: Materializer = ActorMaterializer()

    actorSystem
      .actorOf(ServerLaunchActor.props(actorSystem.actorOf(SchemaManagerActor.props), actorSystem.actorOf(RouteManagerActor.props))) ! ServerLaunchActor.Start
  }
}
