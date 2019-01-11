package com.weaver.api.rest.response

import argonaut.Argonaut._
import argonaut._

case class SearchSparkSessions(sessions: List[SearchSparkSessions.SparkSession])

object SearchSparkSessions {
  case class SparkSession(id: Long, name: String)

  case object SparkSession {
    implicit def SparkSessionCodecJson: CodecJson[SparkSession] =
      casecodec2(SparkSession.apply, SparkSession.unapply)("id", "name")
  }

  implicit def SearchSparkSessionsCodecJson: CodecJson[SearchSparkSessions] =
    casecodec1(SearchSparkSessions.apply, SearchSparkSessions.unapply)("sessions")
}
