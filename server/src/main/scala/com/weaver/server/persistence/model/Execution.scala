package com.weaver.server.persistence.model

import java.time.LocalDateTime
import java.util.UUID

case class Execution(id: UUID = UUID.randomUUID(),
                     method: String,
                     uri: String,
                     startTime: LocalDateTime = LocalDateTime.now(),
                     endTime: Option[LocalDateTime] = None,
                     completed: Boolean = false,
                     succeeded: Boolean = false)
