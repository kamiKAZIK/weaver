package com.kami.weaver.api.rest.response

import argonaut.Argonaut._
import argonaut._

case class SearchBinaries(binaries: List[SearchBinaries.Binary])

object SearchBinaries {
  case class Binary(id: Long, name: String, version: String)

  case object Binary {
    implicit def BinaryCodecJson: CodecJson[Binary] =
      casecodec3(Binary.apply, Binary.unapply)("id", "name", "version")
  }

  implicit def SearchBinariesCodecJson: CodecJson[SearchBinaries] =
    casecodec1(SearchBinaries.apply, SearchBinaries.unapply)("binaries")
}

