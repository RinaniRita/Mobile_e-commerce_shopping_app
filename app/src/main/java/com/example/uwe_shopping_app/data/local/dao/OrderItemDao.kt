package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.uwe_shopping_app.data.local.entity.OrderItemEntity

@Dao
interface OrderItemDao {

    @Insert
    suspend fun insertOrderItem(item: OrderItemEntity)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItemsByOrder(orderId: Int): List<OrderItemEntity>
}