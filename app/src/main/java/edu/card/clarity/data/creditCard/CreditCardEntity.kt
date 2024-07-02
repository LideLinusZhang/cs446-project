package edu.card.clarity.data.creditCard

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity

data class CreditCardEntity(
    @Embedded val creditCardInfo: CreditCardInfoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    val purchaseRewards: List<PurchaseRewardEntity>
)
