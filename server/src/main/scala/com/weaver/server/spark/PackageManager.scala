package com.weaver.server.spark

import java.util.concurrent.CopyOnWriteArraySet

import com.typesafe.config.Config

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

trait PackageManager {
  protected[this] def registry: CopyOnWriteArraySet[String]

  def setUp(config: Config)(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    config.getConfigList("packages").foreach { entry =>
      register(entry.getString("url"))
    }
  }

  def register(url: String): Unit =
    registry.add(url)

  def list: Set[String] =
    registry.toSet
}
