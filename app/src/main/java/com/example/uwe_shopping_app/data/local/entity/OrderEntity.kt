package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val totalPrice: Double,
    val status: String,  // e.g., "pending", "shipped", etc.
    val address: String = "",
    val phoneNumber: String = "",
    val shippingMethod: String = "",
    val shippingFee: Double = 0.0,
    val discountAmount: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
