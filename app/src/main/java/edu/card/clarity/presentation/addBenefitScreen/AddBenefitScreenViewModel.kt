package edu.card.clarity.presentation.addBenefitScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.utils.ArgumentNames
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddBenefitScreenViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val cardIdString: String = savedStateHandle[ArgumentNames.CREDIT_CARD_ID]!!
    private val cardRewardTypeOrdinal: Int = savedStateHandle[ArgumentNames.CREDIT_CARD_REWARD_TYPE]!!

    private val cardId: UUID = UUID.fromString(cardIdString)
    val cardRewardType = RewardType.entries[cardRewardTypeOrdinal]

    val purchaseTypeOptionStrings = PurchaseType.entries.map { it.name }

    private var selectedPurchaseType: PurchaseType = PurchaseType.entries.first()
    private var isFactorValid: Boolean = true
    private var displayedFactor: Float? = null

    private val _uiState = MutableStateFlow(
        AddBenefitScreenUiState(
            selectedPurchaseType = purchaseTypeOptionStrings.first(),
            isFactorValid = true,
            displayedFactor = ""
        )
    )
    val uiState: StateFlow<AddBenefitScreenUiState> = _uiState.asStateFlow()

    fun updateSelectedPurchaseType(index: Int) {
        selectedPurchaseType = PurchaseType.entries[index]

        _uiState.update {
            it.copy(selectedPurchaseType = purchaseTypeOptionStrings[index])
        }
    }

    fun updateFactor(factorInString: String) {
        displayedFactor = factorInString.toFloatOrNull()
        isFactorValid = when (cardRewardType) {
            RewardType.CashBack -> displayedFactor?.let { percentage ->
                percentage > 0.0 && percentage <= 100.0
            } ?: false

            RewardType.PointBack -> displayedFactor?.let { multiplier ->
                multiplier >= 1.0
            } ?: false
        }

        _uiState.update {
            it.copy(
                isFactorValid = isFactorValid,
                displayedFactor = factorInString
            )
        }
    }

    fun addBenefit() {
        viewModelScope.launch {
            if (cardRewardType == RewardType.CashBack) {
                cashBackCreditCardRepository.addPurchaseReward(
                    cardId,
                    listOf(selectedPurchaseType),
                    percentage = displayedFactor!! / 100.0f
                )
                Log.d(
                    "MyBenefitsScreenVM",
                    "New benefit added: ${selectedPurchaseType.name} - ${(displayedFactor!!).toInt()}% cashback"
                )
            } else if (cardRewardType == RewardType.PointBack) {
                pointBackCreditCardRepository.addPurchaseReward(
                    cardId,
                    listOf(selectedPurchaseType),
                    multiplier = displayedFactor!!
                )
                Log.d(
                    "AddBenefitScreenVM",
                    "New benefit added: ${selectedPurchaseType.name} - ${displayedFactor!!}x points"
                )
            }
        }
    }
}