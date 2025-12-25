package com.example.uwe_shopping_app.ui.screens.order

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.ui.components.order.OrderItem
import com.example.uwe_shopping_app.ui.components.order.OrderStatus

class OrderViewModel : ViewModel() {

    private val _orders = mutableStateListOf<OrderItem>()
    val orders: List<OrderItem> = _orders

    init {
        loadOrders()
    }

    private fun loadOrders() {
        _orders.addAll(
            listOf(
                OrderItem("1524", "IK287368838", 2, 110.0, "13/05/2021", OrderStatus.PENDING),
                OrderItem("1514", "IK987362534", 2, 110.0, "13/05/2021", OrderStatus.DELIVERED),
                OrderItem("1671", "IK237368881", 3, 400.0, "10/05/2021", OrderStatus.CANCELLED)
            )
        )
    }

    fun ordersByStatus(status: OrderStatus) =
        orders.filter { it.status == status }
}
