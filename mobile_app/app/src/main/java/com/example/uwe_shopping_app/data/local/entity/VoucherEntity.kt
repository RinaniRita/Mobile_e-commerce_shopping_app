package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vouchers")
data class VoucherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val code: String,
    val title: String,
    val description: String,
    val discountValue: Int,
    val discountType: String, // "PERCENTAGE" or "FIXED"
    val target: String,       // "PRODUCT" or "SHIPPING"
    val usageCount: Int = 0,
    val maxUsage: Int = 3,
    val expiryDay: Int,
    val expiryMonth: String
)
