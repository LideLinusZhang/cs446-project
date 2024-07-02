package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.pointSystem.PointSystemEntity
import java.util.UUID

@Entity(
    tableName = "pointBackCardPointSystemAssociation",
    primaryKeys = ["creditCardId", "pointSystemId"],
    foreignKeys = [
        ForeignKey(
            entity = CreditCardInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PointSystemEntity::class,
            parentColumns = ["id"],
            childColumns = ["pointSystemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("creditCardId", unique = true),
        Index("pointSystemId", unique = true)
    ]
)
data class CreditCardIdPointSystemIdPairEntity(
    val creditCardId: UUID,
    val pointSystemId: UUID,
)
