package com.weaver.api.rest.response

import java.time.LocalDateTime
import java.util.UUID

import io.circe._
import io.circe.generic.semiauto._

import io.circe.java8.time._

case class SearchExecutions(executions: List[SearchExecutions.Execution])

object SearchExecutions {
  case class Execution(id: UUID,
                       method: String,
                       uri: String,
                       startTime: LocalDateTime,
                       endTime: Option[LocalDateTime],
                       completed: Boolean,
                       succeeded: Boolean)


  case object Execution {
    implicit val executionEncoder: Encoder[Execution] = deriveEncoder
    implicit val executionDecoder: Decoder[Execution] = deriveDecoder
  }

  implicit val searchExecutionsEncoder: Encoder[SearchExecutions] = deriveEncoder
  implicit val searchExecutionsDecoder: Decoder[SearchExecutions] = deriveDecoder
}
