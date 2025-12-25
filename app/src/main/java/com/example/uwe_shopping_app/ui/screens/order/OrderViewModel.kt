package com.example.uwe_shopping_app.ui.screens.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.OrderEntity
import com.example.uwe_shopping_app.data.local.repository.OrderRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.order.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class OrderViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val orderDao = App.db.orderDao()
    private val sessionManager = SessionManager(application)

    //  Observe orders for current user
    val orders = sessionManager.userId.flatMapLatest { userId ->
        if (userId == null) {
            kotlinx.coroutines.flow.flowOf(emptyList())
        } else {
            orderDao.getOrdersByUser(userId)
        }
    }


    fun markAsDelivered(orderId: Int) {
        viewModelScope.launch {
            orderDao.updateOrderStatus(
                orderId = orderId,
                status = "delivered"
            )
        }
    }

    fun cancelOrder(orderId: Int) {
        viewModelScope.launch {
            orderDao.updateOrderStatus(
                orderId = orderId,
                status = "cancelled"
            )
        }
    }


}
