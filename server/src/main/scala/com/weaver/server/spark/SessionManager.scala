package com.weaver.server.spark

import com.typesafe.config.{Config, ConfigValue}
import org.apache.spark.sql.SparkSession

import scala.collection.concurrent.TrieMap
import scala.collection.JavaConversions._

object SessionManager {
  val registry: TrieMap[String, SessionManager] = TrieMap.empty

  def setUp(config: Config): Unit = {
    if (config.hasPath("predefined-sessions")) {
      val predefinedSessions = config.getConfig("predefined-sessions")
      predefinedSessions.root.ma
      predefinedSessions.root().entrySet().foreach { entry =>
        get(entry.getKey, entry.getValue)
      }
    }
  }

  def get(name: String, config: ConfigValue): SessionManager =
    registry.getOrElseUpdate(name, new SessionManager(name))
}

class SessionManager(name: String) {
  lazy val session: SparkSession = createSession(name)

  private[this] def createSession(name: String): SparkSession = {
    SparkSession.builder()
      .appName(name)
      .master("local[2]")
      .getOrCreate()
  }
}
