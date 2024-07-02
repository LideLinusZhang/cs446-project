package edu.card.clarity.repositories.creditCard

import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.repositories.utils.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

abstract class CreditCardRepositoryBase internal constructor(
    protected val creditCardDataSource: CreditCardDao,
    private val purchaseRewardDataSource: PurchaseRewardDao,
    protected val dispatcher: CoroutineDispatcher
) {
    suspend fun createCreditCard(info: CreditCardInfo): UUID {
        val id = withContext(dispatcher) {
            UUID.randomUUID()
        }

        val cardInfoEntity = CreditCardInfoEntity(
            id,
            info.name,
            info.rewardType,
            info.cardNetworkType,
            info.statementDate,
            info.paymentDueDate,
        )

        creditCardDataSource.upsert(cardInfoEntity)

        return id
    }

    protected open suspend fun addPurchaseReward(
        creditCardId: UUID,
        rewardType: RewardType,
        purchaseTypes: List<PurchaseType>,
        factor: Float
    ) {
        withContext(dispatcher) {
            for (purchaseType in purchaseTypes) {
                purchaseRewardDataSource.upsert(
                    PurchaseRewardEntity(
                        creditCardId = creditCardId,
                        purchaseType = purchaseType,
                        rewardType = rewardType,
                        factor = factor
                    )
                )
            }
        }
    }

    protected open suspend fun removePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>
    ) {
        withContext(dispatcher) {
            for (purchaseType in purchaseTypes) {
                purchaseRewardDataSource.delete(creditCardId, purchaseType)
            }
        }
    }

    protected open suspend fun updateCreditCardInfo(info: CreditCardInfo) {
        require(info.id != null)

        val cardInfoEntity = creditCardDataSource.getInfoById(info.id)?.copy(
            name = info.name,
            statementDate = info.statementDate,
            paymentDueDate = info.paymentDueDate
        ) ?: throw IllegalArgumentException("Credit card with ID ${info.id} not found.")

        creditCardDataSource.upsert(cardInfoEntity)
    }

    protected open suspend fun getCreditCardInfo(id: UUID): CreditCardInfo? {
        return creditCardDataSource.getInfoById(id)?.toDomainModel()
    }

    protected suspend fun getCreditCardRewardType(id: UUID): RewardType? {
        return creditCardDataSource.getRewardTypeById(id)
    }

    protected suspend fun getAllCreditCardInfoOf(rewardType: RewardType): List<CreditCardInfo> {
        return withContext(dispatcher) {
            creditCardDataSource.getAllInfoOf(rewardType).toDomainModel()
        }
    }

    protected fun getAllCreditCardInfoStreamOf(rewardType: RewardType): Flow<List<CreditCardInfo>> {
        return creditCardDataSource.observeAllInfoOf(rewardType).map {
            withContext(dispatcher) {
                it.toDomainModel()
            }
        }
    }

    protected open suspend fun deleteCreditCard(id: UUID) {
        creditCardDataSource.deleteById(id)
    }

    protected suspend fun deleteAllCreditCardsOf(rewardType: RewardType) {
        creditCardDataSource.deleteAllOf(rewardType)
    }

    protected companion object {
        const val CREDIT_CARD_REWARD_TYPE_IMMUTABLE_ERROR_MESSAGE: String =
            "Cannot change a credit card's reward type."

        fun createCreditCardNotExistErrorMessage(
            creditCardId: UUID,
            rewardType: RewardType
        ): String {
            return "Credit card of ID $creditCardId and reward type $rewardType does not exist or is not a cash back card."
        }
    }
}