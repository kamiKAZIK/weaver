package com.pointlogic.weaver

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.pointlogic.weaver.domain.model.{Binary, SparkSession}

package object json {
  import argonaut._, Argonaut._

  implicit def LocalDateTimeEncodeJson: EncodeJson[LocalDateTime] =
    EncodeJson(d => jString(d.format(DateTimeFormatter.ISO_DATE_TIME)))

  implicit def BinaryEncodeJson: EncodeJson[Binary] =
    EncodeJson((p: Binary) =>
      ("id" := p.id) ->: ("name" := p.name) ->: ("version" := p.version) ->: ("uploaded" := p.uploaded) ->: jEmptyObject)

  implicit def SparkSessionEncodeJson: EncodeJson[SparkSession] =
    EncodeJson((p: SparkSession) =>
      ("id" := p.id) ->: ("name" := p.name) ->: jEmptyObject)
}
