package com.example.uwe_shopping_app.ui.screens.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.repository.CartRepository
import com.example.uwe_shopping_app.data.local.repository.OrderRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CheckoutPaymentViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val cartRepository = CartRepository()
    private val orderRepository = OrderRepository()

    fun placeOrder(
        totalPrice: Double,
        onSuccess: (Long) -> Unit
    ) {
        viewModelScope.launch {
            //  Get user
            val userId = sessionManager.userId.first() ?: return@launch

            //  Get cart
            val cart = cartRepository.getOrCreateCartForUser(userId)

            //  Create order from cart (auto clears cart)
            val orderId = orderRepository.createOrderFromCart(
                userId = userId,
                cartId = cart.id,
                totalPrice = totalPrice
            )

            if (orderId > 0) {
                onSuccess(orderId)
            }
        }
    }
}
