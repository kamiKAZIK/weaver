package com.pointlogic.weaver.data

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DatabaseProvider {
  protected val config: DatabaseConfig[JdbcProfile]
  protected val db: JdbcProfile#Backend#Database = config.db
}
