package com.pointlogic.weaver.route

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.pointlogic.weaver.data.DatabaseConfiguration
import com.pointlogic.weaver.domain.repository.BinariesRepository
import com.pointlogic.weaver.json._
import argonaut._, Argonaut._
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.ExecutionContext

class BinaryRoute(implicit executionContext: ExecutionContext) extends Directives with DatabaseConfiguration {
  private val binaryRepository = new BinariesRepository(config)

  def list: Route = path("binaries") {
    get {
      onSuccess(binaryRepository.findAll.map(_.toList)) { extraction =>
        complete(extraction)
      }
    }
  }
}
