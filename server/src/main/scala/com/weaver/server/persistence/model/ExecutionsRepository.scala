package com.weaver.server.persistence.model

import java.time.LocalDateTime
import java.util.UUID

import com.weaver.server.persistence.connection.{DatabaseConfiguration, DatabaseProvider}
import com.weaver.server.persistence.schema.ExecutionsTable

import scala.concurrent.{ExecutionContext, Future}

trait ExecutionsRepository extends ExecutionsTable { this: DatabaseProvider =>
  import config.profile.api._

  def insert(execution: Execution): Future[Int] = db.run(executions += execution)

  def findAll: Future[Seq[Execution]] = db.run(executions.result)

  def findById(id: UUID)(implicit executionContext: ExecutionContext): Future[Option[Execution]] = db
    .run(executions.filter(_.id === id).result)
    .map(_.headOption)

  def deleteOlderThan(timestamp: LocalDateTime): Future[Int] = db
    .run(executions.filter(_.endTime < timestamp).delete)
}

object ExecutionsRepository extends ExecutionsRepository with DatabaseProvider with DatabaseConfiguration
