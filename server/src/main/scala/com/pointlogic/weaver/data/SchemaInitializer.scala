package com.pointlogic.weaver.data

import com.pointlogic.weaver.data.schema.{BinariesTable, SparkSessionsTable}
import slick.jdbc.meta.MTable

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object SchemaInitializer extends DatabaseConfiguration with DatabaseProvider with BinariesTable with SparkSessionsTable {
  import config.profile.api._

  private val tables = Set(binaries, sparkSessions)

  def init(implicit executionContext: ExecutionContext): Unit =
    Await.result(
      db.run(MTable.getTables).flatMap(mTables =>
        db.run(DBIO.seq(
          tables.filter(table => !mTables.map(_.name.name).contains(table.baseTableRow.tableName))
            .map(_.schema)
            .reduce(_ ++ _)
            .create
        ))
      ),
      5.seconds
    )
}
