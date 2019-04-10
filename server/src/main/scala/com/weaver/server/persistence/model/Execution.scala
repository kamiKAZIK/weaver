package com.weaver.server.persistence.model

import java.time.LocalDateTime
import java.util.UUID

case class Execution(id: UUID, route: String, startTime: LocalDateTime, endTime: LocalDateTime, succeeded: Boolean)
