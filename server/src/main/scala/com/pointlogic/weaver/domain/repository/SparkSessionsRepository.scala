package com.pointlogic.weaver.domain.repository

import com.pointlogic.weaver.data.DatabaseProvider
import com.pointlogic.weaver.data.schema.SparkSessionsTable
import com.pointlogic.weaver.domain.model.SparkSession
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class SparkSessionsRepository(val config: DatabaseConfig[JdbcProfile])(implicit executionContext: ExecutionContext) extends DatabaseProvider with SparkSessionsTable {
  import config.profile.api._

  def insert(sparkSession: SparkSession): Future[Int] = db.run(sparkSessions += sparkSession)

  def findAll: Future[Seq[SparkSession]] = db.run(sparkSessions.result)
}