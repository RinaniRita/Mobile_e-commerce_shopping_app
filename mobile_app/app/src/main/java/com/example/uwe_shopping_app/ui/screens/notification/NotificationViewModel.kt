package com.example.uwe_shopping_app.ui.screens.notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.NotificationEntity
import com.example.uwe_shopping_app.data.local.repository.NotificationRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotificationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val repository = NotificationRepository(sessionManager)
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
