package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uwe_shopping_app.data.local.entity.ProductReviewEntity

@Dao
interface ProductReviewDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertReview(review: ProductReviewEntity)

    @Query("SELECT * FROM product_reviews WHERE productId = :productId")
    suspend fun getReviewsByProduct(productId: Int): List<ProductReviewEntity>

    @Query("""
        SELECT * FROM product_reviews 
        WHERE userId = :userId 
        AND productId = :productId 
        AND orderId = :orderId
        LIMIT 1
    """)
    suspend fun getReview(
        userId: Int,
        productId: Int,
        orderId: Int
    ): ProductReviewEntity?

    @Insert
    suspend fun insert(review: ProductReviewEntity)

    @Query("""
        SELECT AVG(rating) FROM product_reviews 
        WHERE productId = :productId
    """)
    suspend fun getAverageRating(productId: Int): Double?

    @Query("""
        SELECT COUNT(*) FROM product_reviews 
        WHERE productId = :productId
    """)
    suspend fun getReviewCount(productId: Int): Int
}
