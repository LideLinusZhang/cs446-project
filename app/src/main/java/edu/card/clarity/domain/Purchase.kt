package edu.card.clarity.domain

import edu.card.clarity.enums.PurchaseType
import java.util.Date
import java.util.UUID

data class Purchase(
    val id: UUID? = null,
    val time: Date,
    val merchant: String,
    val type: PurchaseType,
    val total: Float,
    val creditCardId: UUID
)
