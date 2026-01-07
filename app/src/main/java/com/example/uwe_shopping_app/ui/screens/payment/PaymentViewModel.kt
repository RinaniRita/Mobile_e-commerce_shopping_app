package com.example.uwe_shopping_app.ui.screens.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.PaymentCardEntity
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.payment.CardType
import com.example.uwe_shopping_app.ui.components.payment.PaymentCardData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class PaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val paymentCardDao = App.db.paymentCardDao()
    private val sessionManager = SessionManager(application)

    val cards: StateFlow<List<PaymentCardData>> = sessionManager.userId
        .flatMapLatest { userId ->
            if (userId != null) {
                paymentCardDao.getCardsByUser(userId).map { entities ->
                    entities.map { it.toUiModel() }
                }
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCard(
        cardholderName: String,
        cardNumber: String,
        expiryMonth: String,
        expiryYear: String,
        cardType: CardType,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // 1. Validate Cardholder Name
        val trimmedName = cardholderName.trim()
        if (trimmedName.isBlank()) {
            onError("Cardholder name is required")
            return
        }
        if (trimmedName.any { it.isDigit() || "!@#$%^&*()".contains(it) }) {
            onError("Name contains invalid characters")
            return
        }
        
        // 2. Validate Card Number
        val cleanCardNumber = cardNumber.replace(" ", "")
        if (cleanCardNumber.length != 16 || !cleanCardNumber.all { it.isDigit() }) {
            onError("Card number must be 16 digits")
            return
        }

        // 3. Validate Expiry Date
        val inputMonth = expiryMonth.toIntOrNull() ?: 0
        val inputYear = expiryYear.toIntOrNull() ?: 0

        if (inputMonth < 1 || inputMonth > 12) {
            onError("Invalid month (01-12)")
            return
        }

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR) % 100
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        if (inputYear < currentYear || (inputYear == currentYear && inputMonth < currentMonth)) {
            onError("Card has expired")
            return
        }
        
        if (inputYear > currentYear + 20) {
            onError("Invalid expiry year")
            return
        }

        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            
            // Duplicate Check
            val currentCards = paymentCardDao.getCardsByUser(userId).first()
            if (currentCards.any { it.cardNumber == cleanCardNumber }) {
                onError("This card is already added")
                return@launch
            }

            val entity = PaymentCardEntity(
                userId = userId,
                cardholderName = trimmedName.uppercase(),
                cardNumber = cleanCardNumber,
                expiryDate = "${expiryMonth.padStart(2, '0')}/${expiryYear}",
                cardType = cardType.name,
                isDefault = currentCards.isEmpty()
            )
            paymentCardDao.insertCard(entity)
            onSuccess()
        }
    }

    fun removeCard(cardId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val cardsForUser = paymentCardDao.getCardsByUser(userId).first()
            val cardToDelete = cardsForUser.find { it.id == cardId }
            
            cardToDelete?.let { 
                paymentCardDao.deleteCard(it)
                // If we deleted the default card, set another one as default if exists
                if (it.isDefault && cardsForUser.size > 1) {
                    val nextCard = cardsForUser.first { c -> c.id != cardId }
                    paymentCardDao.setCardAsDefault(userId, nextCard.id)
                }
            }
        }
    }

    private fun PaymentCardEntity.toUiModel() = PaymentCardData(
        id = id,
        cardNumber = cardNumber,
        cardholderName = cardholderName,
        expiryMonth = expiryDate.split("/").firstOrNull() ?: "",
        expiryYear = expiryDate.split("/").lastOrNull() ?: "",
        cardType = if (cardType == "VISA") CardType.VISA else CardType.MASTERCARD
    )
}
