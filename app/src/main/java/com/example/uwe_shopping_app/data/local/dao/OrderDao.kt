package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.uwe_shopping_app.data.local.entity.OrderEntity

@Dao
interface OrderDao {


    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteOrder(id: Int)
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long  // Returns inserted ID

    @Query("SELECT * FROM orders WHERE userId = :userId")
    suspend fun getOrdersByUser(userId: Int): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Int): OrderEntity?


}