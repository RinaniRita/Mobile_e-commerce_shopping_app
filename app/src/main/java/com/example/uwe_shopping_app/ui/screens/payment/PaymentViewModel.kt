package com.example.uwe_shopping_app.ui.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.ui.components.payment.CardType
import com.example.uwe_shopping_app.ui.components.payment.PaymentCardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Shared ViewModel instance for payment screens
object PaymentViewModelHolder {
    private val viewModel = PaymentViewModel()
    fun getInstance(): PaymentViewModel = viewModel
}

class PaymentViewModel : ViewModel() {
    private val _cards = MutableStateFlow<List<PaymentCardData>>(
        listOf(
            PaymentCardData(
                id = 1,
                cardNumber = "4364134589328378",
                cardholderName = "Sunie Pham",
                expiryMonth = "05",
                expiryYear = "2024",
                cardType = CardType.VISA
            )
        )
    )
    val cards: StateFlow<List<PaymentCardData>> = _cards.asStateFlow()

    fun addCard(card: PaymentCardData) {
        viewModelScope.launch {
            val newId = (_cards.value.maxOfOrNull { it.id } ?: 0) + 1
            _cards.value = _cards.value + card.copy(id = newId)
        }
    }

    fun removeCard(cardId: Int) {
        viewModelScope.launch {
            _cards.value = _cards.value.filter { it.id != cardId }
        }
    }
}

