package edu.card.clarity.repositories

import edu.card.clarity.data.purchase.PurchaseDao
import edu.card.clarity.data.purchase.PurchaseEntity
import edu.card.clarity.dependencyInjection.annotations.DefaultDispatcher
import edu.card.clarity.domain.Purchase
import edu.card.clarity.repositories.utils.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseRepository @Inject constructor(
    private val purchaseDataSource: PurchaseDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun addPurchase(purchase: Purchase): UUID {
        val id = withContext(dispatcher) { UUID.randomUUID() }

        val entity = PurchaseEntity(
            id,
            purchase.time,
            purchase.merchant,
            purchase.type,
            purchase.total,
            purchase.creditCardId
        )

        purchaseDataSource.upsert(entity)

        return id
    }

    suspend fun getPurchase(id: UUID): Purchase? {
        return purchaseDataSource.getById(id)?.toDomainModel()
    }

    suspend fun getAllPurchases(): List<Purchase> {
        return purchaseDataSource.getAll().toDomainModel()
    }

    suspend fun getAllPurchasesOf(creditCardId: UUID): List<Purchase> {
        return purchaseDataSource.getAllOf(creditCardId).toDomainModel()
    }

    suspend fun getAllPurchasesBetween(startTime: Date, endTime: Date): List<Purchase> {
        return if (startTime > endTime) {
            getAllPurchasesBetween(endTime, startTime)
        } else {
            purchaseDataSource.getAllBetween(startTime, endTime).map {
                it.toDomainModel()
            }
        }
    }

    fun getAllPurchasesStream(): Flow<List<Purchase>> {
        return purchaseDataSource.observeAll().map {
            withContext(dispatcher) {
                it.toDomainModel()
            }
        }
    }

    fun getAllPurchasesStreamOf(creditCardId: UUID): Flow<List<Purchase>> {
        return purchaseDataSource.observeAllOf(creditCardId).map {
            withContext(dispatcher) {
                it.toDomainModel()
            }
        }
    }

    fun getAllPurchasesStreamBetween(startTime: Date, endTime: Date): Flow<List<Purchase>> {
        return if (startTime > endTime) {
            getAllPurchasesStreamBetween(endTime, startTime)
        } else {
            purchaseDataSource.observeAllBetween(startTime, endTime).map {
                withContext(dispatcher) {
                    it.toDomainModel()
                }
            }
        }
    }

    suspend fun updatePurchase(purchase: Purchase) {
        require(purchase.id != null)
        require(purchaseDataSource.exist(purchase.id))

        purchaseDataSource.upsert(
            PurchaseEntity(
                purchase.id,
                purchase.time,
                purchase.merchant,
                purchase.type,
                purchase.total,
                purchase.creditCardId
            )
        )
    }

    suspend fun removePurchase(id: UUID) {
        purchaseDataSource.deleteById(id)
    }
}