package com.example.uwe_shopping_app.ui.screens.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.repository.OrderRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.order.OrderItem
import com.example.uwe_shopping_app.ui.components.order.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OrderViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val repository = OrderRepository(sessionManager)

    val isLoggedIn = sessionManager.isLoggedIn

    private val _orderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val orderItems: StateFlow<List<OrderItem>> = _orderItems

    init {
        observeOrders()
    }

    private fun observeOrders() {
        viewModelScope.launch {
            sessionManager.userId
                .flatMapLatest { userId ->
                    if (userId == null) {
                        kotlinx.coroutines.flow.flowOf(emptyList())
                    } else {
                        repository.getOrdersByUser(userId)
                    }
                }
                .collect { orders ->
                    val mapped = orders.map { entity ->
                        OrderItem(
                            orderId = entity.id,
                            subtotal = entity.totalPrice,
                            date = formatDate(entity.createdAt),
                            status = mapStatus(entity.status),
                            totalItems = repository.getTotalItemsForOrder(entity.id)
                        )
                    }
                    _orderItems.value = mapped
                }
        }
    }

    fun markAsDelivered(orderId: Int) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, "delivered")
        }
    }

    fun cancelOrder(orderId: Int) {
        viewModelScope.launch {
            repository.cancelOrder(orderId)
        }
    }

    private fun mapStatus(status: String): OrderStatus =
        when (status.lowercase()) {
            "pending" -> OrderStatus.ON_THE_WAY
            "delivered" -> OrderStatus.DELIVERED
            "cancelled" -> OrderStatus.CANCELLED
            else -> OrderStatus.ON_THE_WAY
        }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
