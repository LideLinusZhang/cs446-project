package edu.card.clarity.repositories.creditCard

import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.ICreditCard
import edu.card.clarity.enums.PurchaseType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ICreditCardRepository {
    suspend fun createCreditCard(info: CreditCardInfo): UUID

    suspend fun addPurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        factor: Float
    )

    suspend fun updatePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        factor: Float
    )

    suspend fun removePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>
    )

    suspend fun updateCreditCardInfo(info: CreditCardInfo)

    suspend fun getCreditCard(id: UUID): ICreditCard?
    suspend fun getCreditCardInfo(id: UUID): CreditCardInfo?

    suspend fun getAllCreditCards(): List<ICreditCard>
    suspend fun getAllCreditCardInfo(): List<CreditCardInfo>

    fun getAllCreditCardsStream(): Flow<List<ICreditCard>>
    fun getAllCreditCardInfoStream(): Flow<List<CreditCardInfo>>

    suspend fun deleteCreditCard(id: UUID)
    suspend fun deleteAllCreditCards()
}