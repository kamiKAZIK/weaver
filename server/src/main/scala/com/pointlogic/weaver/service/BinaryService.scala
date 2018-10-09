package com.pointlogic.weaver.service

import com.pointlogic.weaver.data.DatabaseConfiguration
import com.pointlogic.weaver.domain.model.Binary
import com.pointlogic.weaver.domain.repository.BinariesRepository

import scala.concurrent.{ExecutionContext, Future}

class BinaryService(implicit executionContext: ExecutionContext) extends DatabaseConfiguration {
  private val binaryRepository = new BinariesRepository(config)

  def upload(name: String, payload: Array[Byte]): Unit = {
    //new Binary(name, payload, LocalDateTime.now())
  }

  def fetch(id: Long): Future[Option[Binary]] =
    binaryRepository.findById(id)

  def fetch(name: String, version: String): Future[Option[Binary]] =
    binaryRepository.findByNameAndVersion(name, version)

  def fetch: Future[Seq[Binary]] =
    binaryRepository.findAll
}
