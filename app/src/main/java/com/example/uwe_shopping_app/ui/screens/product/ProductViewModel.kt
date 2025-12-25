package com.example.uwe_shopping_app.ui.screens.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.CartRepository
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.uwe_shopping_app.util.LOGIN_REQUIRED

data class ProductUiState(
    val product: ProductEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isDescriptionExpanded: Boolean = false,
    val isAddToCartSuccess: Boolean = false
)


class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository()
    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val product = productRepository.getProductById(productId)
            _uiState.value = _uiState.value.copy(product = product, isLoading = false)
        }
    }

    // --- HÀM ADD TO CART (GỌN GÀNG) ---
    fun addToCart() {
        val currentProduct = _uiState.value.product ?: return

        viewModelScope.launch {
            val userId = sessionManager.userId.first()

            // NOT LOGGED IN
            if (userId == null) {
                _uiState.value = _uiState.value.copy(error = LOGIN_REQUIRED)
                return@launch
            }

            try {
                val cart = cartRepository.getOrCreateCartForUser(userId)
                cartRepository.addToCart(cart.id, currentProduct.id, 1)

                _uiState.value = _uiState.value.copy(isAddToCartSuccess = true)
                delay(100)
                _uiState.value = _uiState.value.copy(isAddToCartSuccess = false)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Add failed: ${e.message}")
            }
        }
    }


    // Các hàm phụ giữ nguyên
    fun toggleFavorite() { _uiState.value = _uiState.value.copy(isFavorite = !_uiState.value.isFavorite) }
    fun toggleDescriptionExpanded() { _uiState.value = _uiState.value.copy(isDescriptionExpanded = !_uiState.value.isDescriptionExpanded) }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

}