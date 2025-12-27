package com.example.uwe_shopping_app.ui.screens.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.repository.ProductReviewRepository
import com.example.uwe_shopping_app.data.local.repository.WishlistRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.wishlist.WishlistProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WishlistViewModel(application: Application) : AndroidViewModel(application) {

    private val wishlistRepository = WishlistRepository()
    private val reviewRepository = ProductReviewRepository()
    private val sessionManager = SessionManager(application)

    private val _products = MutableStateFlow<List<WishlistProduct>>(emptyList())
    val products: StateFlow<List<WishlistProduct>> = _products

    fun loadWishlist() {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch

            val products = wishlistRepository.getWishlist(userId)

            val uiProducts = products.map { product ->
                val avgRating = reviewRepository.getAverageRating(product.id)
                val reviewCount = reviewRepository.getReviewCount(product.id)

                WishlistProduct(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    imageResId = product.imageResId,
                    rating = avgRating,
                    reviewCount = reviewCount
                )
            }

            _products.value = uiProducts
        }
    }

    fun remove(productId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            wishlistRepository.remove(userId, productId)
            loadWishlist()
        }
    }
}
