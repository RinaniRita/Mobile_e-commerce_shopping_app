package com.example.uwe_shopping_app.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String? = null,
    val imageResId: Int? = null, // For drawable resources (R.drawable.image_name)
    val imageUrls: List<String>? = null, // Multiple image URLs for carousel
    val imageResIds: List<Int>? = null, // Multiple image resource IDs for carousel
    val description: String? = null,
    val category: String? = null,
    val rating: Double = 0.0, // Rating from 0.0 to 5.0
    val reviewCount: Int = 0, // Number of reviews
    val availableColors: List<ProductColor> = emptyList(), // Available color options
    val availableSizes: List<String> = emptyList() // Available sizes (e.g., ["S", "M", "L"])
)

data class ProductColor(
    val name: String,
    val colorValue: Long // Color as Long (e.g., 0xFF000000 for black)
)
