package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.uwe_shopping_app.data.local.entity.OrderItemEntity
import com.example.uwe_shopping_app.data.local.relation.OrderItemWithProduct

@Dao
interface OrderItemDao {

    @Insert
    suspend fun insertOrderItem(item: OrderItemEntity)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItemsByOrder(orderId: Int): List<OrderItemEntity>

    //  join order_items + products
    @Transaction
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItemsWithProducts(
        orderId: Int
    ): List<OrderItemWithProduct>

//    QUANTITY
    @Query("SELECT SUM(quantity) FROM order_items WHERE orderId = :orderId")
    suspend fun getTotalQuantity(orderId: Int): Int?

}
