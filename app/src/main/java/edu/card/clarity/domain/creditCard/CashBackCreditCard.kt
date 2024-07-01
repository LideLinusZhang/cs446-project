package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward

data class CashBackCreditCard(
    override val info: CreditCardInfo,
    override val purchaseRewards: List<PurchaseReward>
) : ICreditCard {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        val purchaseReward = purchaseRewards.first {
            it.applicablePurchaseType == purchase.type
        }

        return purchaseReward.getReturnAmount(purchase)
    }
}