package com.pointlogic.weaver

import com.pointlogic.weaver.domain.model.Binary

package object json {
  import argonaut._, Argonaut._

  implicit def BinaryEncodeJson: EncodeJson[Binary] =
    EncodeJson((p: Binary) =>
      ("id" := p.id) ->: ("name" := p.name) ->: jEmptyObject)
}
