package com.pointlogic.weaver.service

import com.pointlogic.weaver.data.DatabaseConfiguration
import com.pointlogic.weaver.domain.model.SparkSession
import com.pointlogic.weaver.domain.repository.SparkSessionsRepository

import scala.concurrent.{ExecutionContext, Future}

class SparkSessionService(implicit executionContext: ExecutionContext) extends DatabaseConfiguration {
  private val sparkSessionRepository = new SparkSessionsRepository(config)

  def fetch: Future[Seq[SparkSession]] =
    sparkSessionRepository.findAll
}
