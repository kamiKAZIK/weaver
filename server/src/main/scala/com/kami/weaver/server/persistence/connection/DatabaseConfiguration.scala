package com.kami.weaver.server.persistence.connection

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DatabaseConfiguration {
  protected lazy val config: DatabaseConfig[JdbcProfile] = DatabaseConfiguration.instance
}

object DatabaseConfiguration {
  private lazy val instance: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("api-server.database")
}