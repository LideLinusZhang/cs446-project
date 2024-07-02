package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PointBackCardPointSystemAssociationDao {
    /**
     * Observes an association between a point back card of a certain id and a point system.
     *
     * @param id the credit card id.
     * @return the association.
     */
    @Transaction
    @Query("SELECT * FROM pointBackCardPointSystemAssociation WHERE creditCardId = :id")
    fun observeByCreditCardId(id: UUID): Flow<CreditCardPointSystemAssociation>

    /**
     * Gets an association between a point back card of a certain id and a point system.
     *
     * @param id the credit card id.
     * @return the association.
     */
    @Transaction
    @Query("SELECT * FROM pointBackCardPointSystemAssociation WHERE creditCardId = :id")
    suspend fun getByCreditCardId(id: UUID): CreditCardPointSystemAssociation?

    /**
     * Insert or update a pair of credit card and point system ids in the database. If a pair already exists, replace it.
     *
     * @association the pair to insert or update.
     */
    @Upsert
    suspend fun upsert(pair: CreditCardIdPointSystemIdPairEntity)
}