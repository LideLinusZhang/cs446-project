package edu.card.clarity.notifications

import java.time.LocalDateTime
import java.util.UUID

data class SchedulerAlarmItem (
    val id: UUID,
    val time: LocalDateTime,
    val message: String,
    val creditCardId: UUID
)