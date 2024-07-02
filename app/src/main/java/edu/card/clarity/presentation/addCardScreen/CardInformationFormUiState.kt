package edu.card.clarity.presentation.addCardScreen

data class CardInformationFormUiState(
    val cardName: String = "",
    val selectedRewardType: String = "",
    val selectedCardNetworkType: String = "",
    val mostRecentStatementDate: String = "",
    val mostRecentPaymentDueDate: String = "",
    val isReminderEnabled: Boolean = false
)

