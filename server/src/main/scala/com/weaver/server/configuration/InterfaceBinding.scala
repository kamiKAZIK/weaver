package com.weaver.server.configuration

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.weaver.server.routing.{BinaryRoute, ExecutionRoute, RouteManager}

import scala.concurrent.{ExecutionContext, Future}

object InterfaceBinding {
  def bind(address: String)
          (implicit system: ActorSystem, materializer: Materializer, executionContext: ExecutionContext): Future[Http.ServerBinding] =
    Http()
      .bindAndHandle(
        pathPrefix("api") {
          BinaryRoute.routes ~ ExecutionRoute.routes
        } ~ pathPrefix("execution") {
          RouteManager.routes
        },
        address
      )
}
