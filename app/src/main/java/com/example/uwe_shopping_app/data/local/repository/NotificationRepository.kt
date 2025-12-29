package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.NotificationEntity

class NotificationRepository {

    private val notificationDao = App.db.notificationDao()

    fun getNotifications(userId: Int) =
        notificationDao.getNotifications(userId)

    fun getUnreadCount(userId: Int) =
        notificationDao.getUnreadCount(userId)

    suspend fun notify(
        userId: Int,
        title: String,
        message: String,
        type: String
    ) {
        notificationDao.insert(
            NotificationEntity(
                userId = userId,
                title = title,
                message = message,
                type = type
            )
        )
    }

    suspend fun markAsRead(notificationId: Int) {
        notificationDao.markAsRead(notificationId)
    }

    suspend fun markAllAsRead(userId: Int) =
        notificationDao.markAllAsRead(userId)
}
