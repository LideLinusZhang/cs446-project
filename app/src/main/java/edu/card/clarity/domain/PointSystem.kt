package edu.card.clarity.domain

import java.util.UUID

data class PointSystem(
    val id: UUID? = null,
    val name: String,
    val pointToCashConversionRate: Float
) {
    fun getEquivalentCashAmount(pointAmount: Int): Float {
        return pointAmount * pointToCashConversionRate
    }
}