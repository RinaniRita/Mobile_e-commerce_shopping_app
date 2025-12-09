package com.example.uwe_shopping_app.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.database.AppDatabase
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.data.local.repository.UserRepository
import com.example.uwe_shopping_app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val userRepository = UserRepository()

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val email = sessionManager.userEmail.first()
            if (email != null) {
                _user.value = userRepository.getUserByEmail(email)
            }
        }
    }

    fun updateUser(updatedUser: UserEntity) {
        viewModelScope.launch {
            userRepository.updateUser(updatedUser)
            _user.value = updatedUser  // Update local state
            // Update session nếu email thay đổi
            if (updatedUser.email != _user.value?.email) {
                sessionManager.setUserEmail(updatedUser.email)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.setLoggedIn(false)
            sessionManager.setUserEmail(null)
            _user.value = null
        }
    }
}