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

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // 1. Lấy UserID (Giả định luôn có vì đã Login)
            val userId = sessionManager.userId.first() ?: return@launch

            try {
                // 2. Tìm Cart của User
                val cart = cartRepository.getOrCreateCartForUser(userId)

                // 3. Lấy danh sách item
                val cartItemsEntity = cartRepository.getCartItems(cart.id)

                // 4. Ghép dữ liệu Product vào để hiển thị (Tên, Ảnh, Giá)
                val uiItems = cartItemsEntity.mapNotNull { item ->
                    val product = productDao.getProductById(item.productId)
                    if (product != null) {
                        CartItemUiModel(
                            id = item.id,
                            productId = item.productId,
                            name = product.name,
                            price = product.price,
                            imageResId = product.imageResId,
                            size = "L", // Logic size nếu có
                            color = "Default",
                            quantity = item.quantity,
                            isSelected = true
                        )
                    } else null
                }

                calculateTotal(uiItems)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    // Tính tổng tiền
    private fun calculateTotal(items: List<CartItemUiModel>) {
        val total = items.filter { it.isSelected }.sumOf { it.price * it.quantity }
        _uiState.value = _uiState.value.copy(cartItems = items, productPrice = total, isLoading = false)
    }

    // Logic Tăng số lượng
    fun increaseQuantity(itemId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val cart = cartRepository.getOrCreateCartForUser(userId)
            val item = _uiState.value.cartItems.find { it.id == itemId } ?: return@launch

            cartRepository.updateCartItemQuantity(cart.id, item.productId, item.quantity + 1)
            loadCart()
        }
    }

    // Logic Giảm số lượng
    fun decreaseQuantity(itemId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val cart = cartRepository.getOrCreateCartForUser(userId)
            val item = _uiState.value.cartItems.find { it.id == itemId } ?: return@launch

            if (item.quantity > 1) {
                cartRepository.updateCartItemQuantity(cart.id, item.productId, item.quantity - 1)
            } else {
                cartRepository.removeFromCart(cart.id, itemId)
            }
            loadCart()
        }
    }

    // Logic Xóa item
    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val cart = cartRepository.getOrCreateCartForUser(userId)
            cartRepository.removeFromCart(cart.id, itemId)
            loadCart()
        }
    }

    // Logic chọn item (chỉ update UI tạm thời)
    fun toggleItemSelection(itemId: Int) {
        val updated = _uiState.value.cartItems.map {
            if (it.id == itemId) it.copy(isSelected = !it.isSelected) else it
        }
        calculateTotal(updated)
    }
}