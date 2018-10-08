package com.pointlogic.weaver.data

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DatabaseProvider {
  val config: DatabaseConfig[JdbcProfile]
  val db: JdbcProfile#Backend#Database = config.db
}
