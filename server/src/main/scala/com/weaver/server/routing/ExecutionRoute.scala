package com.weaver.server.routing

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.weaver.api.rest.response
import com.weaver.server.persistence.model.{Execution, ExecutionsRepository}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.ExecutionContext

object ExecutionRoute {
  def routes(implicit executionContext: ExecutionContext): Route = list

  private def list(implicit executionContext: ExecutionContext): Route = path("executions") {
    get {
      onSuccess(ExecutionsRepository.findAll.map(executions => response.SearchExecutions(convert(executions)))) { extraction =>
        complete(extraction)
      }
    }
  }

  private def convert(executions: Seq[Execution]): List[response.SearchExecutions.Execution] =
    executions
      .map(execution =>
        response.SearchExecutions.Execution(
          execution.id,
          execution.method,
          execution.uri,
          execution.startTime,
          execution.endTime,
          execution.completed,
          execution.succeeded
        )
      ).toList
}
