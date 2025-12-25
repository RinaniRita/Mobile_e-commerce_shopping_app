package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.uwe_shopping_app.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    //  Observe orders (UI auto-refresh)
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getOrdersByUser(userId: Int): Flow<List<OrderEntity>>

    //  Update ONLY status
    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(
        orderId: Int,
        status: String
    )


    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Int): OrderEntity?

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteOrder(id: Int)
}
