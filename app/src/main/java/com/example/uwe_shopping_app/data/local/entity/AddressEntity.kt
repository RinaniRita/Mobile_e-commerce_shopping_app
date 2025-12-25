package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val recipient: String,
    val addressLine: String,
    val city: String,
    val zipCode: String,
    val phoneNumber: String,
    val type: String, // "HOME" or "OFFICE"
    val isDefault: Boolean = false
)
