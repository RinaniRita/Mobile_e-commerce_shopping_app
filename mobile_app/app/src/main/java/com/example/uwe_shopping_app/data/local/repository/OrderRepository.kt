package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.OrderEntity
import com.example.uwe_shopping_app.data.local.entity.OrderItemEntity
import com.example.uwe_shopping_app.data.local.relation.OrderItemWithProduct
import com.example.uwe_shopping_app.data.local.session.SessionManager

class OrderRepository(
    private val sessionManager: SessionManager
) {

    private val orderDao = App.db.orderDao()
    private val orderItemDao = App.db.orderItemDao()
    private val productDao = App.db.productDao()
    private val cartItemDao = App.db.cartItemDao()
    private val notificationRepo = NotificationRepository(sessionManager)

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

    fun getOrdersByUser(userId: Int) =
        orderDao.getOrdersByUser(userId)

    suspend fun getOrderItemsByOrder(orderId: Int): List<OrderItemEntity> =
        orderItemDao.getOrderItemsByOrder(orderId)


    suspend fun createOrderFromCart(
        userId: Int,
        cartId: Int,
        totalPrice: Double,
        address: String,
        phoneNumber: String,
        shippingMethod: String,
        shippingFee: Double,
        discountAmount: Double,
        status: String = "pending"
    ): Long {
        // Get items by cart
        val cartItems = cartItemDao.getCartItemsByCart(cartId)
        if (cartItems.isEmpty()) return 0L

        // Create order entity
        val order = OrderEntity(
            userId = userId,
            totalPrice = totalPrice,
            status = status,
            address = address,
            phoneNumber = phoneNumber,
            shippingMethod = shippingMethod,
            shippingFee = shippingFee,
            discountAmount = discountAmount
        )
        val orderId = orderDao.insertOrder(order)

        // move item → order_item + reduce stock
        cartItems.forEach { cartItem ->
            val product = productDao.getProductById(cartItem.productId) ?: return 0L

            val orderItem = OrderItemEntity(
                orderId = orderId.toInt(),
                productId = cartItem.productId,
                quantity = cartItem.quantity,
                price = product.price
            )
            orderItemDao.insertOrderItem(orderItem)

            // reduce stock
            val updatedProduct = product.copy(stock = product.stock - cartItem.quantity)
            productDao.updateProduct(updatedProduct)
        }

        // delete cart
        cartItems.forEach { cartItemDao.deleteCartItemById(it.id) }

        return orderId
    }

    suspend fun getOrderById(orderId: Int): OrderEntity? =
        orderDao.getOrderById(orderId)

    suspend fun getOrderItemsWithProducts(
        orderId: Int
    ): List<OrderItemWithProduct> =
        orderItemDao.getOrderItemsWithProducts(orderId)

    suspend fun getTotalItemsForOrder(orderId: Int): Int {
        return orderItemDao.getTotalQuantity(orderId) ?: 0
    }

    suspend fun updateOrderStatus(orderId: Int, newStatus: String) {
        val order = orderDao.getOrderById(orderId) ?: return

        orderDao.updateOrderStatus(orderId, newStatus)


        when (newStatus) {
            "on_the_way" -> {
                notificationRepo.notifyOrderOnTheWay(order.userId, orderId)
            }
            "delivered" -> {
                notificationRepo.notifyOrderDelivered(order.userId, orderId)
            }
            "cancelled" -> {
                notificationRepo.notifyOrderCancelled(order.userId, orderId)
            }
        }
    }

    suspend fun cancelOrder(orderId: Int) {
        val order = orderDao.getOrderById(orderId) ?: return
        if (order.status != "pending") return

        orderDao.updateOrderStatus(
            orderId = orderId,
            status = "cancelled"
        )


        // return stock
        val items = orderItemDao.getOrderItemsByOrder(orderId)
        items.forEach { item ->
            val product = productDao.getProductById(item.productId) ?: return@forEach
            productDao.updateProduct(product.copy(stock = product.stock + item.quantity))
        }
    }
}
