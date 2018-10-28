package com.kami.weaver.server.spark

import org.apache.spark.sql.SparkSession

import scala.collection.concurrent.TrieMap

object SessionManager {
  val registry: TrieMap[String, SessionManager] = TrieMap.empty

  def get(name: String): SessionManager =
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
