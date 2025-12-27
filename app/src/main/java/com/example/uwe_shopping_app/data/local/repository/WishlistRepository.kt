package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.entity.WishlistEntity

class WishlistRepository {

    private val wishlistDao = App.db.wishlistDao()

    suspend fun getWishlist(userId: Int): List<ProductEntity> =
        wishlistDao.getWishlistProducts(userId)

    suspend fun add(userId: Int, productId: Int) {
        wishlistDao.addToWishlist(
            WishlistEntity(userId = userId, productId = productId)
        )
    }

    suspend fun remove(userId: Int, productId: Int) {
        wishlistDao.removeFromWishlist(userId, productId)
    }

    suspend fun isWishlisted(userId: Int, productId: Int): Boolean =
        wishlistDao.isWishlisted(userId, productId)
}
