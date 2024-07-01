package edu.card.clarity.domain.creditCard

import android.icu.util.Calendar
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import java.util.UUID

open class CreditCardInfo(
    val id: UUID? = null,
    val name: String,
    val rewardType: RewardType,
    val cardNetworkType: CardNetworkType,
    val statementDate: Calendar,
    val paymentDueDate: Calendar
)