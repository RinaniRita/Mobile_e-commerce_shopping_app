package com.example.uwe_shopping_app.ui.screens.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.NotificationEntity
import com.example.uwe_shopping_app.data.local.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val repository = NotificationRepository()

    fun getNotifications(userId: Int): Flow<List<NotificationEntity>> {
        return repository.getNotifications(userId)
    }

    fun getUnreadCount(userId: Int): Flow<Int> {
        return repository.getUnreadCount(userId)
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markAsRead(notificationId)
        }
    }

    fun markAllAsRead(userId: Int) {
        viewModelScope.launch {
            repository.markAllAsRead(userId)
        }
    }
}
