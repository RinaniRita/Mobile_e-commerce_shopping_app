package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_cards")
data class PaymentCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val cardholderName: String,
    val cardNumber: String,
    val expiryDate: String, // MM/YY
    val cardType: String,   // VISA, MASTERCARD, etc.
    val isDefault: Boolean = false
)
