package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.NotificationEntity
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.flow.first

class NotificationRepository(
    private val sessionManager: SessionManager
) {

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
        val enabled = sessionManager.notificationsEnabled.first()

//        BLOCK NOTIFICATION
        if (!enabled) return

        notificationDao.insert(
            NotificationEntity(
                userId = userId,
                title = title,
                message = message,
                type = type
            )
        )
    }

    suspend fun notifyOrderOnTheWay(userId: Int, orderId: Int) {
        notificationDao.insert(
            NotificationEntity(
                userId = userId,
                title = "Order is on the way",
                message = "Your order #$orderId is being delivered",
                type = "SHIPPING",
                referenceId = orderId
            )
        )
    }

    suspend fun notifyOrderDelivered(userId: Int, orderId: Int) {
        notificationDao.insert(
            NotificationEntity(
                userId = userId,
                title = "Order delivered",
                message = "Your order #$orderId has arrived",
                type = "ORDER",
                referenceId = orderId
            )
        )
    }

    suspend fun notifyOrderCancelled(userId: Int, orderId: Int) {
        notificationDao.insert(
            NotificationEntity(
                userId = userId,
                title = "Order cancelled",
                message = "Order #$orderId has been cancelled",
                type = "ORDER",
                referenceId = orderId
            )
        )
    }


    suspend fun markAsRead(notificationId: Int) {
        notificationDao.markAsRead(notificationId)
    }

    suspend fun markAllAsRead(userId: Int) =
        notificationDao.markAllAsRead(userId)
}
