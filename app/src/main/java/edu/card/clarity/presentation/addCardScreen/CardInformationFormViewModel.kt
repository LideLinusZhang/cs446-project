package edu.card.clarity.presentation.addCardScreen

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardInformationFormViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
) : ViewModel() {
    val cardNetworkTypeStrings = CardNetworkType.entries.map { it.name }
    val rewardTypeStrings = RewardType.entries.map { it.displayText }

    private val _uiState = MutableStateFlow(
        CardInformationFormUiState(
            selectedCardNetworkType = cardNetworkTypeStrings.first(),
            selectedRewardType = rewardTypeStrings.first()
        )
    )
    val uiState: StateFlow<CardInformationFormUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private var selectedCardNetworkType: CardNetworkType = CardNetworkType.entries.first()
    private var selectedRewardType: RewardType = RewardType.entries.first()
    private val mostRecentStatementDate = Calendar.getInstance()
    private val mostRecentPaymentDueDate = Calendar.getInstance()

    fun updateCardName(name: String) {
        _uiState.update {
            it.copy(cardName = name)
        }
    }

    fun updateSelectedCardNetworkType(index: Int) {
        selectedCardNetworkType = CardNetworkType.entries[index]

        _uiState.update {
            it.copy(selectedCardNetworkType = cardNetworkTypeStrings[index])
        }
    }

    fun updateSelectedRewardType(index: Int) {
        selectedRewardType = RewardType.entries[index]

        _uiState.update {
            it.copy(selectedRewardType = rewardTypeStrings[index])
        }
    }

    fun updateMostRecentStatementDate(year: Int, month: Int, dayOfMonth: Int) {
        mostRecentStatementDate.set(year, month, dayOfMonth)

        _uiState.update {
            it.copy(
                mostRecentStatementDate = dateFormatter.format(mostRecentStatementDate.time)
            )
        }
    }

    fun updateReminderEnabled(isEnabled: Boolean) {
        _uiState.update {
            it.copy(
                isReminderEnabled = isEnabled
            )
        }
    }

    fun updateMostRecentPaymentDueDate(year: Int, month: Int, dayOfMonth: Int) {
        mostRecentPaymentDueDate.set(year, month, dayOfMonth)

        _uiState.update {
            it.copy(
                mostRecentPaymentDueDate = dateFormatter.format(mostRecentPaymentDueDate.time)
            )
        }
    }

    fun addCreditCard() = viewModelScope.launch {
        val creditCardInfo = CreditCardInfo(
            name = uiState.value.cardName,
            rewardType = selectedRewardType,
            cardNetworkType = selectedCardNetworkType,
            statementDate = mostRecentStatementDate,
            paymentDueDate = mostRecentPaymentDueDate
        )

        if (selectedRewardType == RewardType.CashBack) {
            cashBackCreditCardRepository.createCreditCard(creditCardInfo)
        } else {
            pointBackCreditCardRepository.createCreditCard(creditCardInfo)
        }

        _uiState.update {
            CardInformationFormUiState(
                selectedCardNetworkType = cardNetworkTypeStrings.first(),
                selectedRewardType = rewardTypeStrings.first()
            )
        }
    }
}