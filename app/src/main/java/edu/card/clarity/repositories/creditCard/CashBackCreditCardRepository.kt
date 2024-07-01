package edu.card.clarity.repositories.creditCard

import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.dependencyInjection.annotations.DefaultDispatcher
import edu.card.clarity.domain.creditCard.CashBackCreditCard
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.repositories.utils.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CashBackCreditCardRepository @Inject constructor(
    creditCardDataSource: CreditCardDao,
    purchaseReturnDataSource: PurchaseRewardDao,
    @DefaultDispatcher dispatcher: CoroutineDispatcher,
) : CreditCardRepositoryBase(creditCardDataSource, purchaseReturnDataSource, dispatcher),
    ICreditCardRepository {
    override suspend fun addPurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") percentage: Float
    ) {
        require(super.getCreditCardRewardType(creditCardId) == RewardType.CashBack) {
            createCreditCardNotExistErrorMessage(creditCardId, RewardType.CashBack)
        }
        require(percentage > 0.0 && percentage <= 1.0) {
            "Percentage be between 0 and 1 in float"
        }

        super.addPurchaseReward(creditCardId, RewardType.CashBack, purchaseTypes, percentage)
    }

    override suspend fun updatePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") percentage: Float
    ) {
        addPurchaseReward(creditCardId, purchaseTypes, percentage)
    }

    override suspend fun removePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>
    ) {
        if (super.getCreditCardRewardType(creditCardId) == RewardType.CashBack) {

            super.removePurchaseReward(creditCardId, purchaseTypes)
        }
    }

    override suspend fun updateCreditCardInfo(info: CreditCardInfo) {
        require(info.id != null)
        require(info.rewardType == RewardType.CashBack) {
            CREDIT_CARD_REWARD_TYPE_IMMUTABLE_ERROR_MESSAGE
        }
        require(super.getCreditCardRewardType(info.id) == RewardType.CashBack) {
            createCreditCardNotExistErrorMessage(info.id, RewardType.CashBack)
        }

        super.updateCreditCardInfo(info)
    }

    override suspend fun getCreditCard(id: UUID): CashBackCreditCard? {
        return creditCardDataSource.getById(id)?.let {
            if (it.creditCardInfo.rewardType == RewardType.CashBack) {
                CashBackCreditCard(
                    it.creditCardInfo.toDomainModel(),
                    it.purchaseRewards.toDomainModel()
                )
            } else null
        }
    }

    override suspend fun getCreditCardInfo(id: UUID): CreditCardInfo? {
        return creditCardDataSource.getInfoById(id)?.let {
            if (it.rewardType == RewardType.CashBack) {
                it.toDomainModel()
            } else null
        }
    }

    override suspend fun getAllCreditCards(): List<CashBackCreditCard> {
        return creditCardDataSource.getAllOf(RewardType.CashBack).map {
            CashBackCreditCard(
                it.creditCardInfo.toDomainModel(),
                it.purchaseRewards.toDomainModel()
            )
        }
    }

    override suspend fun getAllCreditCardInfo(): List<CreditCardInfo> {
        return super.getAllCreditCardInfoOf(RewardType.CashBack)
    }

    override fun getAllCreditCardsStream(): Flow<List<CashBackCreditCard>> {
        return creditCardDataSource.observeAllOf(RewardType.CashBack).map {
            withContext(dispatcher) {
                it.map {
                    CashBackCreditCard(
                        it.creditCardInfo.toDomainModel(),
                        it.purchaseRewards.toDomainModel()
                    )
                }
            }
        }
    }

    override fun getAllCreditCardInfoStream(): Flow<List<CreditCardInfo>> {
        return super.getAllCreditCardInfoStreamOf(RewardType.CashBack)
    }

    override suspend fun deleteAllCreditCards() {
        return super.deleteAllCreditCardsOf(RewardType.CashBack)
    }

    override suspend fun deleteCreditCard(id: UUID) {
        if (super.getCreditCardRewardType(id) == RewardType.CashBack) {
            super.deleteCreditCard(id)
        }
    }
}