package com.weaver.server.persistence.schema

import java.sql.Timestamp
import java.time.LocalDateTime

import com.weaver.server.persistence.connection.DatabaseProvider

trait Conversions { this: DatabaseProvider =>
  import config.profile.api._

  implicit val localDateTimeToTimestamp: BaseColumnType[LocalDateTime] =
    config.profile.MappedColumnType.base[LocalDateTime, Timestamp](Timestamp.valueOf, _.toLocalDateTime)
}
