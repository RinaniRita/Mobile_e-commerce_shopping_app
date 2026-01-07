package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val message: String,
    val type: String, // VOUCHER, ORDER, SHIPPING
    val referenceId: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
