package edu.card.clarity.data.purchaseReward

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.card.clarity.enums.PurchaseType
import java.util.UUID

@Dao
interface PurchaseRewardDao {
    /**
     * Insert or update a purchase return in the database.
     * If a purchase return already exists, replace it.
     *
     * @param purchaseReturn the purchase return to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(purchaseReturn: PurchaseRewardEntity)

    /**
     * Insert or update purchase returns in the database.
     * If a purchase return already exists, replace it.
     *
     * @param purchaseReturns the purchase returns to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(purchaseReturns: List<PurchaseRewardEntity>)

    /**
     * Update the percentage of a purchase return.
     *
     * @param creditCardId id of the credit card.
     * @param purchaseType purchase type.
     * @param factor factor to be updated.
     */
    @Query(
        "UPDATE purchaseReward " +
                "SET factor = :factor " +
                "WHERE creditCardId = :creditCardId AND purchaseType = :purchaseType"
    )
    suspend fun updateFactor(creditCardId: UUID, purchaseType: PurchaseType, factor: Float)

    /**
     * Delete a purchase return from a credit card.
     *
     * @param creditCardId id of the credit card.
     * @param purchaseType purchase type.
     * @return the number of the purchase return deleted. This should always be 1.
     */
    @Query(
        "DELETE FROM purchaseReward " +
                "WHERE creditCardId = :creditCardId AND purchaseType = :purchaseType"
    )
    suspend fun delete(creditCardId: UUID, purchaseType: PurchaseType): Int
}