package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.ProductReviewEntity

class ProductReviewRepository {

    private val reviewDao = App.db.productReviewDao()

    suspend fun getReview(
        userId: Int,
        productId: Int,
        orderId: Int
    ): ProductReviewEntity? {
        return reviewDao.getReview(userId, productId, orderId)
    }

    suspend fun submitReview(
        userId: Int,
        productId: Int,
        orderId: Int,
        rating: Int,
        comment: String
    ) {
        val existing = reviewDao.getReview(userId, productId, orderId)
        if (existing != null) return

        reviewDao.insert(
            ProductReviewEntity(
                userId = userId,
                productId = productId,
                orderId = orderId,
                rating = rating,
                comment = comment
            )
        )
    }

    suspend fun getReviewsByProduct(productId: Int) =
        reviewDao.getReviewsByProduct(productId)

    suspend fun getAverageRating(productId: Int): Double =
        reviewDao.getAverageRating(productId) ?: 0.0

    suspend fun getReviewCount(productId: Int): Int =
        reviewDao.getReviewCount(productId)

    suspend fun getReviewsWithUsers(productId: Int) =
        reviewDao.getReviewsWithUsers(productId)

}
