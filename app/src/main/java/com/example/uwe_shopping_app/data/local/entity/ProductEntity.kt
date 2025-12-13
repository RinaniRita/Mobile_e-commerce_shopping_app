package com.example.uwe_shopping_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    // String -> Int
    val imageResId: Int,
    val stock: Int,
    val category: String,
    val createdAt: Long = System.currentTimeMillis()
)