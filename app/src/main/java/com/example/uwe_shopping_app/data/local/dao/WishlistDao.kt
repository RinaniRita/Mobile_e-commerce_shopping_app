package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.entity.WishlistEntity

@Dao
interface WishlistDao {

    @Query("""
        SELECT p.* FROM products p
        INNER JOIN wishlist w ON p.id = w.productId
        WHERE w.userId = :userId
        ORDER BY w.createdAt DESC
    """)
    suspend fun getWishlistProducts(userId: Int): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToWishlist(item: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE userId = :userId AND productId = :productId")
    suspend fun removeFromWishlist(userId: Int, productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE userId = :userId AND productId = :productId)")
    suspend fun isWishlisted(userId: Int, productId: Int): Boolean


}
