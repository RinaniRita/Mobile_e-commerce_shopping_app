package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_reviews",
    indices = [Index(value = ["userId", "productId", "orderId"], unique = true)]
)
data class ProductReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val productId: Int,
    val orderId: Int,
    val rating: Int, // 1..5
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
