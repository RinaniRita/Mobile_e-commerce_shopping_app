package com.example.uwe_shopping_app.data.local.model

import androidx.room.Embedded
import com.example.uwe_shopping_app.data.local.entity.ProductEntity

data class ProductWithAvgRating(
    @Embedded val product: ProductEntity,
    val avgRating: Double
)
