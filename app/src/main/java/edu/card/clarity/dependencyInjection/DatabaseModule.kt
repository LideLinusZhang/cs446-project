package edu.card.clarity.dependencyInjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.card.clarity.data.Database
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.purchase.PurchaseDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            name = "card_clarity_db"
        ).build()
    }

    @Provides
    fun providePointSystemDao(database: Database): PointSystemDao = database.pointSystem()

    @Provides
    fun providePurchaseRewardDao(database: Database): PurchaseRewardDao =
        database.purchaseReward()

    @Provides
    fun provideCreditCardDao(database: Database): CreditCardDao =
        database.creditCard()

    @Provides
    fun providePointBackCardPointSystemAssociationDao(
        database: Database
    ): PointBackCardPointSystemAssociationDao = database.pointSystemAssociation()

    @Provides
    fun providePurchaseDao(database: Database): PurchaseDao = database.purchase()
}