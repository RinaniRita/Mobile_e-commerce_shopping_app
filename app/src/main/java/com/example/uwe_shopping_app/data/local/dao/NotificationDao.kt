package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.uwe_shopping_app.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("""
        SELECT COUNT(*) 
        FROM notifications 
        WHERE userId = :userId AND isRead = 0
    """)
    fun getUnreadCount(userId: Int): Flow<Int>

    @Query("""
        SELECT * FROM notifications 
        WHERE userId = :userId 
        ORDER BY createdAt DESC
    """)
    fun getNotifications(userId: Int): Flow<List<NotificationEntity>>

    @Insert
    suspend fun insert(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: Int)
}
