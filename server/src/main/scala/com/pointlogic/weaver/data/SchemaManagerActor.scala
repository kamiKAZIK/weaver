package com.pointlogic.weaver.data

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import com.pointlogic.weaver.data.schema.BinariesTable
import slick.jdbc.meta.MTable

import scala.concurrent.Future

object SchemaManagerActor {
  case object Initialize
  case object Initialized
  case class InitializationError(e: Throwable)

  def props: Props = Props(new SchemaManagerActor)
}

class SchemaManagerActor extends Actor
  with ActorLogging
  with DatabaseConfiguration
  with DatabaseProvider
  with BinariesTable {

  import config.profile.api._
  import context.dispatcher

  override def receive: Receive = {
    case SchemaManagerActor.Initialize =>
      initialize
        .map(_ => SchemaManagerActor.Initialized)
        .recover {
          case e: Throwable =>
            SchemaManagerActor.InitializationError(e)
        }.pipeTo(sender())
    case _ =>
      log.error("Unknown message received!")
  }

  private def initialize: Future[Unit] =
    db.run(MTable.getTables).flatMap(mTables =>
      db.run(DBIO.seq(
        Set(binaries)
          .filter(table => !mTables.map(_.name.name).contains(table.baseTableRow.tableName))
          .map(_.schema)
          .reduce(_ ++ _)
          .create
      ))
    )
}
