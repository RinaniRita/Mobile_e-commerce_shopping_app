package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.OrderEntity
import com.example.uwe_shopping_app.data.local.entity.OrderItemEntity

class OrderRepository {

    private val orderDao = App.db.orderDao()
    private val orderItemDao = App.db.orderItemDao()
    private val productDao = App.db.productDao()
    private val cartItemDao = App.db.cartItemDao()

    suspend fun createOrder(
        userId: Int,
        totalPrice: Double,
        status: String = "pending",
        items: List<OrderItemEntity>
    ): Long {
        val order = OrderEntity(userId = userId, totalPrice = totalPrice, status = status)
        val orderId = orderDao.insertOrder(order)
        items.forEach { item ->
            orderItemDao.insertOrderItem(item.copy(orderId = orderId.toInt()))
        }
        return orderId
    }

    suspend fun getOrdersByUser(userId: Int): List<OrderEntity> =
        orderDao.getOrdersByUser(userId)

    suspend fun getOrderItemsByOrder(orderId: Int): List<OrderItemEntity> =
        orderItemDao.getOrderItemsByOrder(orderId)


    suspend fun createOrderFromCart(
        userId: Int,
        cartId: Int,
        totalPrice: Double,
        status: String = "pending"
    ): Long {
        // 1. Lấy items trong giỏ
        val cartItems = cartItemDao.getCartItemsByCart(cartId)
        if (cartItems.isEmpty()) return 0L

        // 2. Tạo order
        val order = OrderEntity(userId = userId, totalPrice = totalPrice, status = status)
        val orderId = orderDao.insertOrder(order)

        // 3. Chuyển từng item → order_item + giảm stock
        cartItems.forEach { cartItem ->
            val product = productDao.getProductById(cartItem.productId) ?: return 0L

            val orderItem = OrderItemEntity(
                orderId = orderId.toInt(),
                productId = cartItem.productId,
                quantity = cartItem.quantity,
                price = product.price
            )
            orderItemDao.insertOrderItem(orderItem)

            // Giảm tồn kho
            val updatedProduct = product.copy(stock = product.stock - cartItem.quantity)
            productDao.updateProduct(updatedProduct)
        }

        // 4. Xóa sạch giỏ hàng
        cartItems.forEach { cartItemDao.deleteCartItemById(it.id) }

        return orderId
    }

    suspend fun updateOrderStatus(orderId: Int, newStatus: String) {
        val order = orderDao.getOrderById(orderId) ?: return
        orderDao.updateOrder(order.copy(status = newStatus))
    }

    suspend fun cancelOrder(orderId: Int) {
        val order = orderDao.getOrderById(orderId) ?: return
        if (order.status != "pending") return

        orderDao.updateOrder(order.copy(status = "cancelled"))

        // Hoàn lại stock
        val items = orderItemDao.getOrderItemsByOrder(orderId)
        items.forEach { item ->
            val product = productDao.getProductById(item.productId) ?: return@forEach
            productDao.updateProduct(product.copy(stock = product.stock + item.quantity))
        }
    }
}