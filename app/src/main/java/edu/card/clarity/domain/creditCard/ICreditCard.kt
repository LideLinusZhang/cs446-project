package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward

interface ICreditCard {
    val info: CreditCardInfo
    val purchaseRewards: List<PurchaseReward>

    fun getReturnAmountInCash(purchase: Purchase): Float
}