package com.example.uwe_shopping_app.ui.screens.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.CartRepository
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import com.example.uwe_shopping_app.data.local.repository.ProductReviewRepository
import com.example.uwe_shopping_app.data.local.repository.WishlistRepository
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
    val isAddToCartSuccess: Boolean = false,

    val avgRating: Double = 0.0,
    val reviewCount: Int = 0,
    val reviews: List<ProductReviewUi> = emptyList(),
    val showDescription: Boolean = true,
    val showReviews: Boolean = true
)

data class ProductReviewUi(
    val rating: Int,
    val comment: String,
    val userName: String
)


class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository()
    private val reviewRepository = ProductReviewRepository()
    private val wishlistRepository = WishlistRepository()

    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val product = productRepository.getProductById(productId)
            val avg = reviewRepository.getAverageRating(productId) ?: 0.0
            val count = reviewRepository.getReviewCount(productId)
            val reviews = reviewRepository
                .getReviewsWithUsers(productId)
                .map {
                    ProductReviewUi(
                        rating = it.rating,
                        comment = it.comment,
                        userName = it.userName
                    )
                }
            val userId = sessionManager.userId.first()
            val isFav = userId?.let {
                wishlistRepository.isWishlisted(it, productId)
            } ?: false

            _uiState.value = _uiState.value.copy(
                product = product,
                avgRating = avg,
                reviewCount = count,
                reviews = reviews,
                isFavorite = isFav,
                isLoading = false
            )

        }
    }


    fun toggleDescription() {
        _uiState.value = _uiState.value.copy(
            showDescription = !_uiState.value.showDescription
        )
    }


    fun toggleReviews() {
        _uiState.value = _uiState.value.copy(
            showReviews = !_uiState.value.showReviews
        )
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


    fun toggleFavorite() {
        val product = _uiState.value.product ?: return

        viewModelScope.launch {
            val userId = sessionManager.userId.first()

            if (userId == null) {
                _uiState.value = _uiState.value.copy(error = LOGIN_REQUIRED)
                return@launch
            }

            val isWishlisted = wishlistRepository.isWishlisted(userId, product.id)

            if (isWishlisted) {
                wishlistRepository.remove(userId, product.id)
            } else {
                wishlistRepository.add(userId, product.id)
            }

            _uiState.value = _uiState.value.copy(isFavorite = !isWishlisted)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

}