package com.weaver.api.rest.response

import io.circe._
import io.circe.generic.semiauto._

case class SearchBinaries(binaries: List[SearchBinaries.Binary])

object SearchBinaries {
  case class Binary(uri: String)

  case object Binary {
    implicit val binaryEncoder: Encoder[Binary] = deriveEncoder
    implicit val binaryDecoder: Decoder[Binary] = deriveDecoder
  }

  implicit val searchBinariesEncoder: Encoder[SearchBinaries] = deriveEncoder
  implicit val searchBinariesDecoder: Decoder[SearchBinaries] = deriveDecoder
}
