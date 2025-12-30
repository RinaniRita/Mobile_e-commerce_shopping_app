package com.example.uwe_shopping_app.ui.screens.review

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.repository.OrderRepository
import com.example.uwe_shopping_app.data.local.repository.ProductReviewRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.launch

data class ReviewUiState(
    val rating: Int = 0,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val submitted: Boolean = false,
    val alreadyReviewed: Boolean = false,
    val error: String? = null
)

data class ProductReviewUi(
    val productId: Int,
    val name: String,
    val rating: Int = 0,
    val comment: String = "",
    val alreadyReviewed: Boolean = false,
    val isSubmitting: Boolean = false
)


class ReviewViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    private val reviewRepo = ProductReviewRepository()
    private val orderRepo = OrderRepository(sessionManager)

    var items by mutableStateOf<List<ProductReviewUi>>(emptyList())
        private set

    fun load(orderId: Int, userId: Int) {
        viewModelScope.launch {
            val orderItems = orderRepo.getOrderItemsWithProducts(orderId)

            val mapped = orderItems.map { item ->
                val existing = reviewRepo.getReview(
                    userId = userId,
                    productId = item.product.id,
                    orderId = orderId
                )

                ProductReviewUi(
                    productId = item.product.id,
                    name = item.product.name,
                    rating = existing?.rating ?: 0,
                    comment = existing?.comment.orEmpty(),
                    alreadyReviewed = existing != null
                )
            }

            items = mapped
        }
    }

    fun setRating(productId: Int, rating: Int) {
        items = items.map {
            if (it.productId == productId)
                it.copy(rating = rating)
            else it
        }
    }

    fun setComment(productId: Int, comment: String) {
        items = items.map {
            if (it.productId == productId)
                it.copy(comment = comment)
            else it
        }
    }

    fun submit(productId: Int, userId: Int, orderId: Int) {
        val item = items.find { it.productId == productId } ?: return
        if (item.alreadyReviewed || item.rating == 0) return

        viewModelScope.launch {
            reviewRepo.submitReview(
                userId = userId,
                productId = productId,
                orderId = orderId,
                rating = item.rating,
                comment = item.comment
            )

            items = items.map {
                if (it.productId == productId)
                    it.copy(alreadyReviewed = true)
                else it
            }
        }
    }
}

