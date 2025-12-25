package com.example.uwe_shopping_app.ui.screens.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.repository.CartRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.cart.CartItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CartUiState(
    val cartItems: List<CartItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val productPrice: Double = 0.0
)

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartRepository = CartRepository()
    private val sessionManager = SessionManager(application)
    private val productDao = App.db.productDao()

    // Expose login state to CartScreen
    val isLoggedIn = sessionManager.isLoggedIn

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        observeLoginState()
    }

    // ---------------- LOGIN OBSERVER ----------------
    private fun observeLoginState() {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { loggedIn ->
                if (loggedIn) {
                    loadCart()
                } else {
                    _uiState.value = CartUiState()
                }
            }
        }
    }

    // ---------------- LOAD CART ----------------
    fun loadCart() {
        viewModelScope.launch {
            val userId = sessionManager.userId.first()

            // 👇 Guest user → empty cart
            if (userId == null) {
                _uiState.value = CartUiState()
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 1. Get or create cart
                val cart = cartRepository.getOrCreateCartForUser(userId)

                // 2. Load cart items
                val cartItemsEntity = cartRepository.getCartItems(cart.id)

                // 3. Map to UI model
                val uiItems = cartItemsEntity.mapNotNull { item ->
                    val product = productDao.getProductById(item.productId)
                    product?.let {
                        CartItemUiModel(
                            id = item.id,
                            productId = item.productId,
                            name = it.name,
                            price = it.price,
                            imageResId = it.imageResId,
                            size = "L",
                            color = "Default",
                            quantity = item.quantity,
                            isSelected = true
                        )
                    }
                }

                calculateTotal(uiItems)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    // ---------------- TOTAL PRICE ----------------
    private fun calculateTotal(items: List<CartItemUiModel>) {
        val total = items
            .filter { it.isSelected }
            .sumOf { it.price * it.quantity }

        _uiState.value = _uiState.value.copy(
            cartItems = items,
            productPrice = total,
            isLoading = false
        )
    }

    // ---------------- INCREASE ----------------
    fun increaseQuantity(itemId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val cart = cartRepository.getOrCreateCartForUser(userId)
            val item = _uiState.value.cartItems.find { it.id == itemId } ?: return@launch

            cartRepository.updateCartItemQuantity(
                cart.id,
                item.productId,
                item.quantity + 1
            )

            loadCart()
        }
    }

    // ---------------- DECREASE ----------------
    fun decreaseQuantity(itemId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val cart = cartRepository.getOrCreateCartForUser(userId)
            val item = _uiState.value.cartItems.find { it.id == itemId } ?: return@launch

            if (item.quantity > 1) {
                cartRepository.updateCartItemQuantity(
                    cart.id,
                    item.productId,
                    item.quantity - 1
                )
            } else {
                cartRepository.removeFromCart(cart.id, itemId)
            }

            loadCart()
        }
    }

    // ---------------- REMOVE ----------------
    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val cart = cartRepository.getOrCreateCartForUser(userId)

            cartRepository.removeFromCart(cart.id, itemId)
            loadCart()
        }
    }

    // ---------------- SELECT ----------------
    fun toggleItemSelection(itemId: Int) {
        val updatedItems = _uiState.value.cartItems.map {
            if (it.id == itemId) it.copy(isSelected = !it.isSelected) else it
        }
        calculateTotal(updatedItems)
    }
}
