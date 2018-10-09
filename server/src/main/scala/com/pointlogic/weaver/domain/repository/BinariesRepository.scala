package com.pointlogic.weaver.domain.repository

import com.pointlogic.weaver.data.DatabaseProvider
import com.pointlogic.weaver.data.schema.BinariesTable
import com.pointlogic.weaver.domain.model.Binary
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class BinariesRepository(val config: DatabaseConfig[JdbcProfile])(implicit executionContext: ExecutionContext) extends DatabaseProvider with BinariesTable {
  import config.profile.api._

  def insert(binary: Binary): Future[Int] = db.run(binaries += binary)

  def findAll: Future[Seq[Binary]] = db.run(binaries.result)

  def findById(id: Long): Future[Option[Binary]] = db
    .run(binaries.filter(_.id === id).result)
    .map(_.headOption)

  def findByNameAndVersion(name: String, version: String): Future[Option[Binary]] = db
    .run(binaries.filter(b => b.name === name && b.version === version).result)
    .map(_.headOption)
}