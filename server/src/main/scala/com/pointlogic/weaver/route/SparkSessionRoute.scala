package com.pointlogic.weaver.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import argonaut.Argonaut._
import com.pointlogic.weaver.json._
import com.pointlogic.weaver.service.SparkSessionService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.ExecutionContext

class SparkSessionRoute(implicit executionContext: ExecutionContext) {
  private val sparkSessionService: SparkSessionService = new SparkSessionService

  def routes: Route = list

  private def list: Route = path("spark-sessions") {
    get {
      onSuccess(sparkSessionService.fetch.map(_.toList)) { extraction =>
        complete(extraction)
      }
    }
  }
}
