package edu.card.clarity.data.purchaseReward

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import java.util.UUID

@Entity(
    tableName = "purchaseReward",
    primaryKeys = ["creditCardId", "purchaseType"],
    foreignKeys = [
        ForeignKey(
            entity = CreditCardInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("creditCardId", unique = true),
        Index("rewardType", unique = false),
    ]
)
data class PurchaseRewardEntity(
    val creditCardId: UUID,
    val purchaseType: PurchaseType,
    val rewardType: RewardType,
    val factor: Float
)