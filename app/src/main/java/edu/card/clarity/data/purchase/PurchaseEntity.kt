package edu.card.clarity.data.purchase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import edu.card.clarity.data.converters.DateConverter
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.enums.PurchaseType
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "purchase",
    foreignKeys = [
        ForeignKey(
            entity = CreditCardInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"]
        )
    ],
    indices = [
        Index("creditCardId"),
        Index("time")
    ]
)
@TypeConverters(DateConverter::class)
data class PurchaseEntity(
    @PrimaryKey val id: UUID,
    val time: Date,
    val merchant: String,
    val type: PurchaseType,
    val total: Float,
    val creditCardId: UUID
)
