package com.pointlogic.weaver.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import argonaut.Argonaut._
import com.pointlogic.weaver.json._
import com.pointlogic.weaver.service.BinaryService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.ExecutionContext

class BinaryRoute(implicit executionContext: ExecutionContext) {
  private val binaryService: BinaryService = new BinaryService

  def routes: Route = list

  private def list: Route = path("binaries") {
    get {
      onSuccess(binaryService.fetch.map(_.toList)) { extraction =>
        complete(extraction)
      }
    }
  }
}
