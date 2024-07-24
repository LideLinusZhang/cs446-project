package edu.card.clarity.presentation.recordReceiptScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.card.clarity.data.receipt.Receipt
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.data.receipt.ReceiptDao
import edu.card.clarity.presentation.utils.scanReceipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

@HiltViewModel
class RecordReceiptViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository
    private val receiptDao: ReceiptDao,
    @ApplicationContext private val context: Context,
    ) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordReceiptUiState())
    val uiState: StateFlow<RecordReceiptUiState> = _uiState.asStateFlow()

    private val _allCards = combine(
        cashBackCreditCardRepository.getAllCreditCardInfoStream(),
        pointBackCreditCardRepository.getAllCreditCardInfoStream()
    ) { cashBack, pointBack ->
        cashBack + pointBack
    }.stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    val allCards: StateFlow<List<CreditCardInfo>> = _allCards

    private val _showCamera = MutableStateFlow(false)
    val showCamera: StateFlow<Boolean> = _showCamera.asStateFlow()

    fun onCameraError(error: String) {
        _uiState.value = _uiState.value.copy(cameraError = error)
    }

    fun resetCameraError() {
        _uiState.value = _uiState.value.copy(cameraError = null)
    }

    fun onImageCaptured(imagePath: String) {
        _uiState.value = _uiState.value.copy(photoPath = imagePath)
        _showCamera.value = false
    }

    fun openCamera() {
        _uiState.value = _uiState.value.copy(showCamera = true)
    }

    fun resetCamera() {
        _showCamera.value = true
    }

//    fun scanReceipt() {
//        // TOOD: Implement receipt scanning logic
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(
//                date = "2024-07-20",
//                totalAmount = "45.99",
//                merchant = "Local Store"
//            )
//        }
//    }

    fun onDateChange(newDate: String) {
        _uiState.value = _uiState.value.copy(date = newDate)
    }

    fun onTotalAmountChange(newTotalAmount: String) {
        _uiState.value = _uiState.value.copy(totalAmount = newTotalAmount)
    }

    fun onMerchantChange(newMerchant: String) {
        _uiState.value = _uiState.value.copy(merchant = newMerchant)
    }

    fun onCardSelected(newCard: CreditCardInfo) {
        _uiState.value = _uiState.value.copy(selectedCard = newCard)
    }

    fun onPurchaseTypeSelected(newPurchaseType: String) {
        _uiState.value = _uiState.value.copy(selectedPurchaseType = newPurchaseType)
    }

    fun addReceipt() {
        viewModelScope.launch {
            val receipt = Receipt(
                date = _uiState.value.date,
                totalAmount = _uiState.value.totalAmount,
                merchant = _uiState.value.merchant,
                selectedCardId = _uiState.value.selectedCard!!.id!!,
                selectedPurchaseType = _uiState.value.selectedPurchaseType,
                photoPath = _uiState.value.photoPath
            )
            receiptDao.insertReceipt(receipt)

            // Wrap scanReceipt in a coroutine
            withContext(Dispatchers.IO) {
                scanReceipt(context, "receipt_1.jpg")
            }
        }
    }
}

data class RecordReceiptUiState(
    val date: String = "",
    val totalAmount: String = "",
    val merchant: String = "",
    val selectedCard: CreditCardInfo? = null,
    val selectedPurchaseType: String = "",
    val photoPath: String? = null,
    val showCamera: Boolean = false,
    val cameraError: String? = null
)
