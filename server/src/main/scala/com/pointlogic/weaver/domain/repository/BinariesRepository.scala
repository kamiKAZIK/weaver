package com.pointlogic.weaver.domain.repository

import com.pointlogic.weaver.data.DatabaseProvider
import com.pointlogic.weaver.data.schema.BinariesTable
import com.pointlogic.weaver.domain.model.Binary
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class BinariesRepository(val config: DatabaseConfig[JdbcProfile]) extends DatabaseProvider with BinariesTable {
  import config.driver.api._

  def insert(binary: Binary): Future[Int] = db.run(binaries += binary)

  def findAll: Future[Seq[Binary]] = db.run(binaries.result)

  def findById(id: Long)(implicit executionContext: ExecutionContext): Future[Option[Binary]] = db
    .run(binaries.filter(_.id === id).result)
    .map(_.headOption)

  def findByName(name: String)(implicit executionContext: ExecutionContext): Future[Option[Binary]] = db
    .run(binaries.filter(_.name === name).result)
    .map(_.headOption)
}