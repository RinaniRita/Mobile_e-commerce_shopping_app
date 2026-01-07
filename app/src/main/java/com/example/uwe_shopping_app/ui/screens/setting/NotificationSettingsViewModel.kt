package com.example.uwe_shopping_app.ui.screens.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.repository.NotificationRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.launch

class NotificationSettingViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val repository = NotificationRepository(sessionManager)

    val notificationsEnabled = sessionManager.notificationsEnabled

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            sessionManager.setNotificationsEnabled(enabled)
        }
    }
}
