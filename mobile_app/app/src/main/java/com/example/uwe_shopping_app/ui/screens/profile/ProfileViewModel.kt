package com.example.uwe_shopping_app.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.UserEntity
import com.example.uwe_shopping_app.data.local.repository.UserRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.payment.CardType
import com.example.uwe_shopping_app.ui.components.payment.PaymentCardData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val userRepository = UserRepository(sessionManager)
    private val paymentCardDao = App.db.paymentCardDao()

    // ---------------- AUTH STATE ----------------
    val isLoggedIn: StateFlow<Boolean> =
        sessionManager.isLoggedIn
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    // ---------------- USER STATE ----------------
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    // ---------------- PAYMENT CARD STATE ----------------
    val defaultCard: StateFlow<PaymentCardData?> = sessionManager.userId
        .flatMapLatest { userId ->
            if (userId != null) {
                paymentCardDao.getCardsByUser(userId).map { entities ->
                    entities.find { it.isDefault }?.let { entity ->
                        PaymentCardData(
                            id = entity.id,
                            cardNumber = entity.cardNumber,
                            cardholderName = entity.cardholderName,
                            expiryMonth = entity.expiryDate.split("/").firstOrNull() ?: "",
                            expiryYear = entity.expiryDate.split("/").lastOrNull() ?: "",
                            cardType = if (entity.cardType == "VISA") CardType.VISA else CardType.MASTERCARD
                        )
                    }
                }
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        observeLoginState()
    }

    // ---------------- OBSERVE LOGIN ----------------
    private fun observeLoginState() {
        viewModelScope.launch {
            isLoggedIn.collect { loggedIn ->
                if (loggedIn) {
                    loadUserProfile()
                } else {
                    _user.value = null
                }
            }
        }
    }

    // ---------------- LOAD USER ----------------
    private suspend fun loadUserProfile() {
        val userEmail = sessionManager.userEmail.first()

        if (userEmail != null) {
            val userEntity = userRepository.getUserByEmail(userEmail)
            _user.value = userEntity
        }
    }

    // ---------------- UPDATE USER ----------------
    fun updateUser(updatedUser: UserEntity) {
        viewModelScope.launch {
            userRepository.updateUser(updatedUser)

            _user.value = updatedUser

            // Keep session in sync (important)
            sessionManager.saveUserSession(
                id = updatedUser.id,
                email = updatedUser.email,
                phone = updatedUser.phone
            )
        }
    }

    // ---------------- LOGOUT ----------------
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _user.value = null
        }
    }
}
