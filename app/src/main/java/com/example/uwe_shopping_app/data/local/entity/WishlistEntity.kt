package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wishlist",
    indices = [Index(value = ["userId", "productId"], unique = true)]
)
data class WishlistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val productId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
