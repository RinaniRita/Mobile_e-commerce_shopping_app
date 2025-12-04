package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.CartEntity
import com.example.uwe_shopping_app.data.local.entity.CartItemEntity

class CartRepository {

    private val cartDao = App.db.cartDao()
    private val cartItemDao = App.db.cartItemDao()

    suspend fun getOrCreateCartForUser(userId: Int): CartEntity {
        var cart = cartDao.getCartByUser(userId)
        if (cart == null) {
            cart = CartEntity(userId = userId)
            val cartId = cartDao.insertCart(cart)
            cart = cart.copy(id = cartId.toInt())
        }
        return cart
    }

    suspend fun addToCart(cartId: Int, productId: Int, quantity: Int) {
        val existingItem = cartItemDao.getCartItemByProduct(cartId, productId)
        if (existingItem != null) {
            val updated = existingItem.copy(quantity = existingItem.quantity + quantity)
            cartItemDao.updateCartItem(updated)
        } else {
            val item = CartItemEntity(cartId = cartId, productId = productId, quantity = quantity)
            cartItemDao.insertCartItem(item)
        }
    }

    suspend fun getCartItems(cartId: Int): List<CartItemEntity> {
        return cartItemDao.getCartItemsByCart(cartId)
    }

    suspend fun removeFromCart(cartId: Int, itemId: Int) {
        cartItemDao.deleteCartItemById(itemId)
    }

    suspend fun updateCartItemQuantity(cartId: Int, productId: Int, newQuantity: Int) {
        val item = cartItemDao.getCartItemByProduct(cartId, productId) ?: return
        if (newQuantity <= 0) {
            cartItemDao.deleteCartItemById(item.id)
        } else {
            val updated = item.copy(quantity = newQuantity)
            cartItemDao.updateCartItem(updated)
        }
    }

    suspend fun calculateCartTotal(cartId: Int): Double {
        val items = getCartItems(cartId)
        return items.sumOf { item ->
            val product = App.db.productDao().getProductById(item.productId) ?: return 0.0
            item.quantity * product.price
        }
    }

    suspend fun clearCart(cartId: Int) {
        val items = getCartItems(cartId)
        items.forEach { cartItemDao.deleteCartItemById(it.id) }
    }
}