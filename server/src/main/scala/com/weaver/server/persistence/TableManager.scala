package com.weaver.server.persistence

import com.weaver.server.persistence.connection.{DatabaseConfiguration, DatabaseProvider}
import com.weaver.server.persistence.schema.{BinariesTable, ExecutionsTable}
import slick.jdbc.meta.MTable

import scala.concurrent.{ExecutionContext, Future}

trait TableManager extends BinariesTable with ExecutionsTable { this: DatabaseProvider =>
  import config.profile.api._

  def setUp(implicit executionContext: ExecutionContext): Future[Unit] =
    db.run(MTable.getTables).flatMap(mTables =>
      db.run(DBIO.seq(
        Set(binaries, executions)
          .filter(table => !mTables.map(_.name.name).contains(table.baseTableRow.tableName))
          .map(_.schema)
          .reduce(_ ++ _)
          .create
      ))
    )
}

object TableManager extends TableManager with DatabaseProvider with DatabaseConfiguration
