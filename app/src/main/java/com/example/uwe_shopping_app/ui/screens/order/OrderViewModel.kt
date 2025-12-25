package com.example.uwe_shopping_app.ui.screens.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.OrderEntity
import com.example.uwe_shopping_app.data.local.repository.OrderRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OrderRepository()
    private val sessionManager = SessionManager(application)

    private val _orders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val orders: StateFlow<List<OrderEntity>> = _orders

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            _orders.value = repository.getOrdersByUser(userId)
        }
    }
}
