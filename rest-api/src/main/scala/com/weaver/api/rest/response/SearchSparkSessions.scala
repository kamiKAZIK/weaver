package com.weaver.api.rest.response

import io.circe._
import io.circe.generic.semiauto._

case class SearchSparkSessions(sessions: List[SearchSparkSessions.SparkSession])

object SearchSparkSessions {
  case class SparkSession(id: Long, name: String)

  case object SparkSession {
    implicit val sparkSessionEncoder: Encoder[SparkSession] = deriveEncoder
    implicit val sparkSessionDecoder: Decoder[SparkSession] = deriveDecoder
  }

  implicit val searchSparkSessionsEncoder: Encoder[SearchSparkSessions] = deriveEncoder
  implicit val searchSparkSessionsDecoder: Decoder[SearchSparkSessions] = deriveDecoder
}
