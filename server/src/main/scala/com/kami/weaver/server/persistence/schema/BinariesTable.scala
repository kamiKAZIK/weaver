package com.kami.weaver.server.persistence.schema

import java.sql.Timestamp
import java.time.LocalDateTime

import com.kami.weaver.server.persistence.connection.DatabaseProvider
import com.kami.weaver.server.persistence.model.Binary
import slick.ast.BaseTypedType
import slick.lifted.{Index, ProvenShape}

trait BinariesTable { this: DatabaseProvider =>
  import config.profile.api._

  private[BinariesTable] class Binaries(tag: Tag) extends Table[Binary](tag, "BINARIES") {
    private[this] implicit val localDateTimeToTimestamp: BaseTypedType[LocalDateTime] =
      config.profile.MappedColumnType.base[LocalDateTime, Timestamp](Timestamp.valueOf, _.toLocalDateTime)

    def id: Rep[Long] = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("NAME", O.Length(128))
    def version: Rep[String] = column[String]("VERSION", O.Length(32))
    def payload: Rep[Array[Byte]] = column[Array[Byte]]("NAME")
    def uploaded: Rep[LocalDateTime] = column[LocalDateTime]("UPLOADED")

    def nameVersionIndex: Index = index("BINARY_NAME_VERSION_IDX", (name, version), unique = true)

    def * : ProvenShape[Binary] = (id, name, version, payload, uploaded) <> (Binary.tupled, Binary.unapply)
  }

  val binaries = TableQuery[Binaries]
}
