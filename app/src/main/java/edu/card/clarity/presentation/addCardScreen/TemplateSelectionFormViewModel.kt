package edu.card.clarity.presentation.addCardScreen

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.creditCard.CashBackCreditCard
import edu.card.clarity.domain.creditCard.ICreditCard
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.presentation.utils.displayString
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TemplateSelectionFormViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
) : ViewModel() {
    private val cashBackTemplates = cashBackCreditCardRepository
        .getAllPredefinedCreditCardsStream()
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    val templateOptionStrings: StateFlow<List<String>> = cashBackTemplates
        .map { it.map { card -> card.info.name } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private val mostRecentStatementDate = Calendar.getInstance()
    private val mostRecentPaymentDueDate = Calendar.getInstance()

    private var selectedTemplate: ICreditCard? = null

    private val _uiState = MutableStateFlow(TemplateSelectionFormUiState())
    val uiState = _uiState.asStateFlow()

    fun updateTemplateSelection(selectedIndex: Int) {

        selectedTemplate = cashBackTemplates.value[selectedIndex]
        (selectedTemplate as CashBackCreditCard).info.let {
            _uiState.value = _uiState.value.copy(
                selectedTemplateName = it.name,
                showCardInfo = true,
                cardName = it.name,
                rewardType = it.rewardType.displayString,
                cardNetworkType = it.cardNetworkType.name
            )
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

    fun updateMostRecentPaymentDueDate(year: Int, month: Int, dayOfMonth: Int) {
        mostRecentPaymentDueDate.set(year, month, dayOfMonth)

        _uiState.update {
            it.copy(
                mostRecentPaymentDueDate = dateFormatter.format(mostRecentPaymentDueDate.time)
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
}
