package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward

class PointBackCreditCard(
    override val info: CreditCardInfo,
    override val purchaseRewards: List<PurchaseReward>,
    val pointSystem: PointSystem
) : ICreditCard {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        return getReturnAmountInPoint(purchase) * pointSystem.pointToCashConversionRate
    }

    fun getReturnAmountInPoint(purchase: Purchase): Int {
        val purchaseReturn =
            purchaseRewards.first { it.applicablePurchaseType == purchase.type }

        return purchaseReturn.getReturnAmount(purchase).toInt()
    }
}