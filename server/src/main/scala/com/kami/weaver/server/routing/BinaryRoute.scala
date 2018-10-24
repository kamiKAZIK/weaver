package com.kami.weaver.server.routing

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.kami.weaver.api.rest.response
import com.kami.weaver.server.persistence.model.{BinariesRepository, Binary}
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.ExecutionContext

object BinaryRoute {
  def routes(implicit executionContext: ExecutionContext): Route = list

  private def list(implicit executionContext: ExecutionContext): Route = path("binaries") {
    get {
      onSuccess(BinariesRepository.findAll.map(binaries => response.SearchBinaries(convert(binaries)))) { extraction =>
        complete(extraction)
      }
    }
  }

  private def convert(binaries: Seq[Binary]): List[response.SearchBinaries.Binary] =
    binaries
      .map(binary =>
        response.SearchBinaries.Binary(
          binary.id,
          binary.name,
          binary.version
        )
      ).toList
}
