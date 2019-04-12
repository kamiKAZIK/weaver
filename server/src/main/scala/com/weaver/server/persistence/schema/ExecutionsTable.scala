package com.weaver.server.persistence.schema

import java.time.LocalDateTime
import java.util.UUID

import com.weaver.server.persistence.connection.DatabaseProvider
import com.weaver.server.persistence.model.Execution
import slick.lifted.ProvenShape

trait ExecutionsTable extends Conversions { this: DatabaseProvider =>
  import config.profile.api._

  private[ExecutionsTable] class Executions(tag: Tag) extends Table[Execution](tag, "EXECUTIONS") {
    def id: Rep[UUID] = column[UUID]("ID", O.PrimaryKey)
    def method: Rep[String] = column[String]("METHOD", O.Length(32))
    def uri: Rep[String] = column[String]("ROUTE", O.Length(256))
    def startTime: Rep[LocalDateTime] = column[LocalDateTime]("START_TIME")
    def endTime: Rep[Option[LocalDateTime]] = column[Option[LocalDateTime]]("END_TIME")
    def completed: Rep[Boolean] = column[Boolean]("COMPLETED")
    def succeeded: Rep[Boolean] = column[Boolean]("SUCCEEDED")

    def * : ProvenShape[Execution] = (id, method, uri, startTime, endTime, completed, succeeded) <> (Execution.tupled, Execution.unapply)
  }

  val executions = TableQuery[Executions]
}
