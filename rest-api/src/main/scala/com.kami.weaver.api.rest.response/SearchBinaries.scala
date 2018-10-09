package com.kami.weaver.api.rest.response

import com.kami.weaver.api.rest.response.SearchBinaries.Binary
import argonaut.{Argonaut, _}

case class SearchBinaries(binaries: List[Binary])

object SearchBinaries {
  case class Binary(id: Long, name: String, version: String)
}

