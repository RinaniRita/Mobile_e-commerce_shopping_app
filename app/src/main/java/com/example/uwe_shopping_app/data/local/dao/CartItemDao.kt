package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.uwe_shopping_app.data.local.entity.CartItemEntity

@Dao
interface CartItemDao {

    @Insert
    suspend fun insertCartItem(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId")
    suspend fun getCartItemsByCart(cartId: Int): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE cartId = :cartId AND productId = :productId LIMIT 1")
    suspend fun getCartItemByProduct(cartId: Int, productId: Int): CartItemEntity?

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItemById(id: Int)
}