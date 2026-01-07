package com.example.uwe_shopping_app.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.uwe_shopping_app.data.local.entity.OrderItemEntity
import com.example.uwe_shopping_app.data.local.entity.ProductEntity

data class OrderItemWithProduct(
    @Embedded
    val orderItem: OrderItemEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)
