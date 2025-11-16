package com.example.uwe_shopping_app.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String? = null,
    val imageResId: Int? = null, // For drawable resources (R.drawable.image_name)
    val description: String? = null,
    val category: String? = null
)
