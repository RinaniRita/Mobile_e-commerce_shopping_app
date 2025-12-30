package com.example.uwe_shopping_app.ui.screens.orderInfo

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
import com.example.uwe_shopping_app.ui.components.order.OrderStatus
import kotlinx.coroutines.launch


data class OrderInfoUiState(
    val orderId: Int = 0,
    val status: OrderStatus = OrderStatus.ON_THE_WAY,
    val deliveryAddress: String = "",
    val trackingNumber: String = "",
    val items: List<OrderItemUi> = emptyList(),
    val subtotal: Double = 0.0,
    val shipping: Double = 0.0
)

data class OrderItemUi(
    val productId: Int,
    val name: String,
    val quantity: Int,
    val price: Double
)

class OrderInfoViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    private val repository = OrderRepository(sessionManager)
    private val reviewRepository = ProductReviewRepository()

    var uiState by mutableStateOf(OrderInfoUiState())
        private set

    fun loadOrder(orderId: Int) {
        viewModelScope.launch {
            val order = repository.getOrderById(orderId) ?: return@launch
            val items = repository.getOrderItemsWithProducts(orderId)

            uiState = uiState.copy(
                orderId = order.id,
                status = mapStatus(order.status),
                trackingNumber = "TRK-${order.id}",
                deliveryAddress = "Saved address",
                items = items.map {
                    OrderItemUi(
                        productId = it.product.id,
                        name = it.product.name,
                        quantity = it.orderItem.quantity,
                        price = it.orderItem.price
                    )
                },
                subtotal = order.totalPrice,
                shipping = 0.0
            )
        }
    }

    fun submitReview(
        userId: Int,
        productId: Int,
        rating: Int,
        comment: String
    ) {
        if (uiState.status != OrderStatus.DELIVERED) return

        viewModelScope.launch {
            reviewRepository.submitReview(
                userId = userId,
                productId = productId,
                orderId = uiState.orderId,
                rating = rating,
                comment = comment
            )
        }
    }

    private fun mapStatus(status: String): OrderStatus =
        when (status.lowercase()) {
            "pending" -> OrderStatus.ON_THE_WAY
            "delivered" -> OrderStatus.DELIVERED
            "cancelled" -> OrderStatus.CANCELLED
            else -> OrderStatus.ON_THE_WAY
        }
}


