package edu.card.clarity.repositories.creditCard

import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.ICreditCard
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPair
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.pointSystem.PointSystem
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.dependencyInjection.annotations.DefaultDispatcher
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.PointBackCreditCard
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.repositories.utils.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointBackCreditCardRepository @Inject constructor(
    creditCardDataSource: CreditCardDao,
    purchaseReturnDataSource: PurchaseRewardDao,
    private val pointSystemDataSource: PointSystemDao,
    private val pointSystemAssociationDataSource: PointBackCardPointSystemAssociationDao,
    @DefaultDispatcher dispatcher: CoroutineDispatcher,
) : CreditCardRepositoryBase(creditCardDataSource, purchaseReturnDataSource, dispatcher),
    ICreditCardRepository {
    suspend fun createCreditCard(info: CreditCardInfo, pointSystemId: UUID): UUID {
        require(pointSystemDataSource.exist(pointSystemId))

        val creditCardId = super.createCreditCard(info)

        pointSystemAssociationDataSource.upsert(
            CreditCardIdPointSystemIdPair(creditCardId, pointSystemId)
        )

        return creditCardId
    }

    override suspend fun addPurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") multiplier: Float
    ) {
        require(super.getCreditCardRewardType(creditCardId) == RewardType.PointBack) {
            createCreditCardNotExistErrorMessage(creditCardId, RewardType.PointBack)
        }
        require(multiplier >= 1.0) {
            "Multiplier must be greater than or equal to 1."
        }

        super.addPurchaseReward(creditCardId, RewardType.PointBack, purchaseTypes, multiplier)
    }

    override suspend fun updatePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") multiplier: Float
    ) {
        addPurchaseReward(creditCardId, purchaseTypes, multiplier)
    }

    override suspend fun removePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>
    ) {
        if (super.getCreditCardRewardType(creditCardId) == RewardType.PointBack) {

            super.removePurchaseReward(creditCardId, purchaseTypes)
        }
    }

    override suspend fun updateCreditCardInfo(info: CreditCardInfo) {
        require(info.id != null)
        require(info.rewardType == RewardType.PointBack) {
            CREDIT_CARD_REWARD_TYPE_IMMUTABLE_ERROR_MESSAGE
        }
        require(super.getCreditCardRewardType(info.id) == RewardType.PointBack) {
            createCreditCardNotExistErrorMessage(info.id, RewardType.PointBack)
        }

        super.updateCreditCardInfo(info)
    }

    override suspend fun getCreditCard(id: UUID): PointBackCreditCard? {
        return creditCardDataSource.getById(id)?.let {
            if (it.creditCardInfo.rewardType == RewardType.PointBack) {
                val pointSystemEntity = pointSystemAssociationDataSource
                    .getByCreditCardId(id)
                    ?.pointSystem!!

                it.toDomainModel(pointSystemEntity)
            } else null
        }
    }

    override suspend fun getCreditCardInfo(id: UUID): CreditCardInfo? {
        return creditCardDataSource.getInfoById(id)?.let {
            if (it.rewardType == RewardType.PointBack) {
                it.toDomainModel()
            } else null
        }
    }

    override suspend fun getAllCreditCards(): List<PointBackCreditCard> {
        return creditCardDataSource.getAllOf(RewardType.PointBack).map {
            val pointSystemEntity = pointSystemAssociationDataSource
                .getByCreditCardId(it.creditCardInfo.id)
                ?.pointSystem!!

            it.toDomainModel(pointSystemEntity)
        }
    }

    override suspend fun getAllPredefinedCreditCards(): List<PointBackCreditCard> {
        return creditCardDataSource.getAllPredefinedOf(RewardType.PointBack).map {
            val pointSystemEntity = pointSystemAssociationDataSource
                .getByCreditCardId(it.creditCardInfo.id)
                ?.pointSystem!!

            it.toDomainModel(pointSystemEntity).removeId()
        }
    }

    override suspend fun getAllCreditCardInfo(): List<CreditCardInfo> {
        return super.getAllCreditCardInfoOf(RewardType.PointBack)
    }

    override fun getCreditCardStream(id: UUID): Flow<PointBackCreditCard> {
        return creditCardDataSource.observeById(id).mapNotNull {
            if (it.creditCardInfo.rewardType == RewardType.PointBack) {
                val pointSystemEntity = pointSystemAssociationDataSource
                    .getByCreditCardId(id)
                    ?.pointSystem!!

                it.toDomainModel(pointSystemEntity)
            } else null
        }
    }

    override fun getAllCreditCardsStream(): Flow<List<PointBackCreditCard>> {
        return creditCardDataSource.observeAllOf(RewardType.PointBack).map {
            withContext(dispatcher) {
                it.map {
                    val pointSystemEntity = pointSystemAssociationDataSource
                        .getByCreditCardId(it.creditCardInfo.id)
                        ?.pointSystem!!

                    it.toDomainModel(pointSystemEntity)
                }
            }
        }
    }

    override fun getAllPredefinedCreditCardsStream(): Flow<List<PointBackCreditCard>> {
        return creditCardDataSource.observeAllPredefinedOf(RewardType.PointBack).map {
            withContext(dispatcher) {
                it.map {
                    val pointSystemEntity = pointSystemAssociationDataSource
                        .getByCreditCardId(it.creditCardInfo.id)
                        ?.pointSystem!!

                    it.toDomainModel(pointSystemEntity).removeId()
                }
            }
        }
    }

    override fun getAllCreditCardInfoStream(): Flow<List<CreditCardInfo>> {
        return super.getAllCreditCardInfoStreamOf(RewardType.PointBack)
    }

    override suspend fun deleteAllCreditCards() {
        return super.deleteAllCreditCardsOf(RewardType.PointBack)
    }

    override suspend fun deleteCreditCard(id: UUID) {
        if (super.getCreditCardRewardType(id) == RewardType.PointBack) {
            super.deleteCreditCard(id)
        }
    }

    private companion object {
        private fun ICreditCard.toDomainModel(
            pointSystem: PointSystem
        ) = PointBackCreditCard(
            this.creditCardInfo.toDomainModel(),
            this.purchaseRewards.toDomainModel(),
            pointSystem.toDomainModel()
        )

        private fun PointBackCreditCard.removeId(): PointBackCreditCard = this.copy(
            info = this.info.copy(id = null)
        )
    }
}