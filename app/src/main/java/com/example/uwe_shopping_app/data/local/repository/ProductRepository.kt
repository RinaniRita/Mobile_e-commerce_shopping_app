package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.ProductEntity

class ProductRepository {

    private val productDao = App.db.productDao()

    suspend fun getProductsPaged(offset: Int, limit: Int = 20): List<ProductEntity> {
        return productDao.getProductsPaged(offset, limit)
    }

    suspend fun getProductsByCategory(category: String, offset: Int, limit: Int = 20): List<ProductEntity> {
        return productDao.getProductsByCategory(category, offset, limit)
    }

    suspend fun getProductsByPriceRange(minPrice: Double, maxPrice: Double, offset: Int, limit: Int = 20): List<ProductEntity> {
        return productDao.getProductsByPrice(minPrice, maxPrice, offset, limit)
    }

    suspend fun getProductsSorted(sortBy: String, sortOrder: String, offset: Int, limit: Int = 20): List<ProductEntity> {

        return if (sortOrder.equals("ASC", ignoreCase = true)) {
            productDao.getProductsSortedAsc(sortBy, limit, offset)
        } else {
            productDao.getProductsSortedDesc(sortBy, limit, offset)
        }
    }

    suspend fun searchProducts(query: String, offset: Int, limit: Int = 20): List<ProductEntity> {
        return productDao.searchProducts(query, offset, limit)
    }

    suspend fun getProductById(id: Int): ProductEntity? {
        return productDao.getProductById(id)
    }

    suspend fun getProductCount(): Int {
        return productDao.getProductCount()
    }

    suspend fun insertAll(products: List<ProductEntity>) {
        productDao.insertAll(products)
    }
}
