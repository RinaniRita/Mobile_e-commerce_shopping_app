package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uwe_shopping_app.data.local.entity.ProductReviewEntity
import com.example.uwe_shopping_app.data.local.model.ReviewWithUser

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

    @Query("""
    SELECT r.rating, r.comment, u.name AS userName
    FROM product_reviews r
    INNER JOIN users u ON r.userId = u.id
    WHERE r.productId = :productId
    ORDER BY r.createdAt DESC
""")
    suspend fun getReviewsWithUsers(productId: Int): List<ReviewWithUser>

}
