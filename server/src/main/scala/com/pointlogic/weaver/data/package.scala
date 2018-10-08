package com.pointlogic.weaver

import java.sql.Timestamp
import java.time.LocalDateTime

import slick.ast.BaseTypedType
import slick.driver.H2Driver.api._
import slick.jdbc.H2Profile.MappedColumnType

package object data {
  implicit val localDateTimeToTimestamp: BaseTypedType[LocalDateTime] =
    MappedColumnType.base[LocalDateTime, Timestamp](Timestamp.valueOf, _.toLocalDateTime)
}
