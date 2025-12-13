package com.example.uwe_shopping_app.ui.screens.cart

import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.ui.components.cart.CartItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CartUiState(
    val cartItems: List<CartItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val productPrice: Double = 0.0
)

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        // Mock cart data - matching the image
        val mockCartItems = listOf(
            CartItemUiModel(
                id = 1,
                productId = 1,
                name = "Sportwear Set",
                price = 80.00,
                imageResId = android.R.drawable.ic_menu_gallery, // Replace with actual drawable
                size = "L",
                color = "Cream",
                quantity = 1,
                isSelected = true
            ),
            CartItemUiModel(
                id = 2,
                productId = 2,
                name = "Turtleneck Sweater",
                price = 39.99,
                imageResId = android.R.drawable.ic_menu_gallery, // Replace with actual drawable
                size = "M",
                color = "White",
                quantity = 1,
                isSelected = true
            ),
            CartItemUiModel(
                id = 3,
                productId = 3,
                name = "Cotton T-shirt",
                price = 30.00,
                imageResId = android.R.drawable.ic_menu_gallery, // Replace with actual drawable
                size = "L",
                color = "Black",
                quantity = 1,
                isSelected = true
            )
        )

        // Calculate total from selected items
        val total = mockCartItems
            .filter { it.isSelected }
            .sumOf { it.price * it.quantity }

        _uiState.value = _uiState.value.copy(
            cartItems = mockCartItems,
            productPrice = total,
            isLoading = false,
            error = null
        )
    }

    fun toggleItemSelection(itemId: Int) {
        val updatedItems = _uiState.value.cartItems.map { item ->
            if (item.id == itemId) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }
        val total = updatedItems
            .filter { it.isSelected }
            .sumOf { it.price * it.quantity }
        _uiState.value = _uiState.value.copy(
            cartItems = updatedItems,
            productPrice = total
        )
    }

    fun decreaseQuantity(itemId: Int) {
        val updatedItems = _uiState.value.cartItems.map { item ->
            if (item.id == itemId) {
                val newQuantity = (item.quantity - 1).coerceAtLeast(1)
                item.copy(quantity = newQuantity)
            } else {
                item
            }
        }
        val total = updatedItems
            .filter { it.isSelected }
            .sumOf { it.price * it.quantity }
        _uiState.value = _uiState.value.copy(
            cartItems = updatedItems,
            productPrice = total
        )
    }

    fun increaseQuantity(itemId: Int) {
        val updatedItems = _uiState.value.cartItems.map { item ->
            if (item.id == itemId) {
                item.copy(quantity = item.quantity + 1)
            } else {
                item
            }
        }
        val total = updatedItems
            .filter { it.isSelected }
            .sumOf { it.price * it.quantity }
        _uiState.value = _uiState.value.copy(
            cartItems = updatedItems,
            productPrice = total
        )
    }

    fun removeItem(itemId: Int) {
        val updatedItems = _uiState.value.cartItems.filter { it.id != itemId }
        val total = updatedItems
            .filter { it.isSelected }
            .sumOf { it.price * it.quantity }
        _uiState.value = _uiState.value.copy(
            cartItems = updatedItems,
            productPrice = total
        )
    }

    fun proceedToCheckout() {
        // This will be handled by navigation
    }
}
