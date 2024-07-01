package edu.card.clarity.data.pointSystem

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPairEntity
import java.util.UUID

data class PointSystemWithCreditCardIds(
    @Embedded val pointSystem: PointSystemEntity,
    @Relation(
        entity = CreditCardIdPointSystemIdPairEntity::class,
        parentColumn = "id",
        entityColumn = "pointSystemId",
        projection = ["creditCardId"]
    )
    val creditCardIds: List<UUID>
)
