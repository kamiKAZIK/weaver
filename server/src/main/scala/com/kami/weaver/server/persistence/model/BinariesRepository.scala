package com.kami.weaver.server.persistence.model

import com.kami.weaver.server.persistence.connection.{DatabaseConfiguration, DatabaseProvider}
import com.kami.weaver.server.persistence.schema.BinariesTable

import scala.concurrent.{ExecutionContext, Future}

trait BinariesRepository extends BinariesTable { this: DatabaseProvider =>
  import config.profile.api._

  def insert(binary: Binary): Future[Int] = db.run(binaries += binary)

  def findAll: Future[Seq[Binary]] = db.run(binaries.result)

  def findById(id: Long)(implicit executionContext: ExecutionContext): Future[Option[Binary]] = db
    .run(binaries.filter(_.id === id).result)
    .map(_.headOption)

  def findByNameAndVersion(name: String, version: String)(implicit executionContext: ExecutionContext): Future[Option[Binary]] = db
    .run(binaries.filter(b => b.name === name && b.version === version).result)
    .map(_.headOption)
}

object BinariesRepository extends BinariesRepository with DatabaseProvider with DatabaseConfiguration
