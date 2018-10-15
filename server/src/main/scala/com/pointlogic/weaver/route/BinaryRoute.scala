package com.pointlogic.weaver.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.kami.weaver.api.rest.response
import com.pointlogic.weaver.service.BinaryService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._
import com.pointlogic.weaver.domain.model
import scala.concurrent.ExecutionContext

class BinaryRoute(implicit executionContext: ExecutionContext) {
  private val binaryService: BinaryService = new BinaryService

  def routes: Route = list

  private def list: Route = path("binaries") {
    get {
      onSuccess(binaryService.fetch.map(binaries => response.SearchBinaries(convert(binaries)))) { extraction =>
        complete(extraction)
      }
    }
  }

  private def convert(binaries: Seq[model.Binary]): List[response.SearchBinaries.Binary] =
    binaries
      .map(binary =>
        response.SearchBinaries.Binary(
          binary.id,
          binary.name,
          binary.version
        )
      ).toList
}
