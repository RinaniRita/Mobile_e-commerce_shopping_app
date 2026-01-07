package com.example.uwe_shopping_app.data.local.dao

import androidx.room.*
import com.example.uwe_shopping_app.data.local.entity.PaymentCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentCardDao {
    @Query("SELECT * FROM payment_cards WHERE userId = :userId")
    fun getCardsByUser(userId: Int): Flow<List<PaymentCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: PaymentCardEntity)

    @Delete
    suspend fun deleteCard(card: PaymentCardEntity)

    @Query("UPDATE payment_cards SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultCards(userId: Int)

    @Query("UPDATE payment_cards SET isDefault = 1 WHERE id = :cardId")
    suspend fun setDefaultCard(cardId: Int)

    @Transaction
    suspend fun setCardAsDefault(userId: Int, cardId: Int) {
        clearDefaultCards(userId)
        setDefaultCard(cardId)
    }
}
