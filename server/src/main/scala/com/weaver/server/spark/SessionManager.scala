package com.weaver.server.spark

import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConversions._
import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}

object SessionManager {
  val registry: TrieMap[String, SessionManager] = TrieMap.empty

  def setUp(config: Config, packages: Set[String])(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    config.getConfigList("sessions").foreach { entry =>
      register(
        entry.getString("name"),
        entry.getString("master"),
        entry.getConfig("conf"),
        packages
      )
    }
  }

  def register(name: String, master: String, config: Config, packages: Set[String]): Unit =
    registry.putIfAbsent(name, new SessionManager(name, master, config, packages))

  def get(name: String): SessionManager =
    registry(name)
}

class SessionManager(name: String, master: String, config: Config, packages: Set[String]) {
  val session: SparkSession = createSession(name, master, config, packages)

  private[this] def createSession(name: String, master: String, config: Config, packages: Set[String]): SparkSession = {
    val session = SparkSession.builder()
      .appName(name)
      .master(master)
      .getOrCreate()
    packages.foreach(session.sparkContext.addJar)
    session
  }
}
