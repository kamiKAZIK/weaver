package com.kami.weaver.server.persistence.model

import java.time.LocalDateTime

case class Binary(id: Long, name: String, version: String, payload: Array[Byte], uploaded: LocalDateTime)
