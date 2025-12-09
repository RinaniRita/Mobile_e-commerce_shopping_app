package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.uwe_shopping_app.data.local.entity.CartEntity

@Dao
interface CartDao {

    @Insert
    suspend fun insertCart(cart: CartEntity): Long  // Returns inserted ID

    @Query("SELECT * FROM carts WHERE userId = :userId LIMIT 1")
    suspend fun getCartByUser(userId: Int): CartEntity?
}