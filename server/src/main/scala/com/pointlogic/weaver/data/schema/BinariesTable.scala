package com.pointlogic.weaver.data.schema

import java.time.LocalDateTime

import com.pointlogic.weaver.data._
import com.pointlogic.weaver.domain.model.Binary
import slick.lifted.{Index, ProvenShape}

trait BinariesTable { this: DatabaseProvider =>
  import config.driver.api._

  private class Binaries(tag: Tag) extends Table[Binary](tag, "BINARIES") {
    def id: Rep[Long] = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("NAME", O.Length(128))
    def payload: Rep[Array[Byte]] = column[Array[Byte]]("NAME")
    def uploaded: Rep[LocalDateTime] = column[LocalDateTime]("UPLOADED")

    def nameIndex: Index = index("BINARY_NAME_IDX", name, unique = true)

    def * : ProvenShape[Binary] = (id, name, payload, uploaded) <> (Binary.tupled, Binary.unapply)
  }

  val binaries = TableQuery[Binaries]
}
