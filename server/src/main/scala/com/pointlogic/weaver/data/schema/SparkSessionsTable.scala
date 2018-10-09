package com.pointlogic.weaver.data.schema

import com.pointlogic.weaver.data._
import com.pointlogic.weaver.domain.model.SparkSession
import slick.lifted.{Index, ProvenShape}

trait SparkSessionsTable { this: DatabaseProvider =>
  import config.profile.api._

  protected class SparkSessions(tag: Tag) extends Table[SparkSession](tag, "SPARK_SESSIONS") {
    def id: Rep[Long] = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("NAME", O.Length(128))

    def nameVersionIndex: Index = index("SPARK_SESSION_NAME_IDX", name, unique = true)

    def * : ProvenShape[SparkSession] = (id, name) <> (SparkSession.tupled, SparkSession.unapply)
  }

  val sparkSessions = TableQuery[SparkSessions]
}
