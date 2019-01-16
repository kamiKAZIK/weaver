package com.weaver.server.spark

import com.typesafe.config.{Config, ConfigValue}
import org.apache.spark.sql.SparkSession

import scala.collection.concurrent.TrieMap
import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

object SessionManager {
  val registry: TrieMap[String, SessionManager] = TrieMap.empty

  def setUp(config: Config)(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    if (config.hasPath("predefined-sessions")) {
      config.getConfigList("predefined-sessions")
        .foreach(entry => register(entry.getString("name"), entry.getString("master"), entry.getConfig("conf")))
    }
  }

  def register(name: String, master: String, config: Config): Unit =
    registry.putIfAbsent(name, new SessionManager(name, master, config))

  def get(name: String): SessionManager =
    registry(name)
}

class SessionManager(name: String, master: String, config: Config) {
  lazy val session: SparkSession = createSession(name, master, config)

  private[this] def createSession(name: String, master: String, config: Config): SparkSession = {
    SparkSession.builder()
      .appName(name)
      .master(master)
      .getOrCreate()
  }
}
