package com.weaver.server.routing

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.weaver.api.rest.response
import com.weaver.server.spark.PackageManager
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.ExecutionContext

object BinaryRoute {
  def routes(implicit executionContext: ExecutionContext): Route = list

  private def list(implicit executionContext: ExecutionContext): Route = path("binaries") {
    get {
      complete(response.SearchBinaries(convert(PackageManager.list)))
    }
  }

  private def convert(binaries: Set[String]): List[response.SearchBinaries.Binary] =
    binaries
      .map(binary => response.SearchBinaries.Binary(binary))
      .toList
}
