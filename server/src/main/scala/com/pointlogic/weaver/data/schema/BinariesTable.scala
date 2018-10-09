package com.pointlogic.weaver.data.schema

import java.time.LocalDateTime

import com.pointlogic.weaver.data._
import com.pointlogic.weaver.domain.model.Binary
import slick.lifted.{Index, ProvenShape}

trait BinariesTable { this: DatabaseProvider =>
  import config.profile.api._

  protected class Binaries(tag: Tag) extends Table[Binary](tag, "BINARIES") {
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
