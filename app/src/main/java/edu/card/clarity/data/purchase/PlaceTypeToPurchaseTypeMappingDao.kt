package edu.card.clarity.data.purchase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.card.clarity.enums.PurchaseType
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceTypeToPurchaseTypeMappingDao {
    @Query("SELECT purchaseType FROM placeTypeToPurchaseTypeMapping WHERE placeType = :placeType")
    suspend fun observePurchaseTypeByPlaceType(placeType: String): Flow<PurchaseType>

    @Query("SELECT purchaseType FROM placeTypeToPurchaseTypeMapping WHERE placeType = :placeType")
    fun getPurchaseTypeByPlaceType(placeType: String): PurchaseType?

    @Upsert
    fun upsert(mapping: PlaceTypeToPurchaseTypeMappingEntity)
}