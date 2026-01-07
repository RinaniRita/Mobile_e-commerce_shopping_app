package com.example.uwe_shopping_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.model.ProductWithAvgRating

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products LIMIT :limit OFFSET :offset")
    suspend fun getProductsPaged(offset: Int, limit: Int): List<ProductEntity>

    @Query("SELECT * FROM products WHERE category = :category LIMIT :limit OFFSET :offset")
    suspend fun getProductsByCategory(category: String, offset: Int, limit: Int): List<ProductEntity>

    @Query("SELECT * FROM products WHERE price BETWEEN :minPrice AND :maxPrice LIMIT :limit OFFSET :offset")
    suspend fun getProductsByPrice(minPrice: Double, maxPrice: Double, offset: Int, limit: Int): List<ProductEntity>

    //ASC
    @Query("SELECT * FROM products ORDER BY CASE WHEN :sortBy = 'price' THEN price WHEN :sortBy = 'name' THEN name WHEN :sortBy = 'stock' THEN stock WHEN :sortBy = 'createdAt' THEN createdAt END ASC LIMIT :limit OFFSET :offset")
    suspend fun getProductsSortedAsc(sortBy: String, limit: Int, offset: Int): List<ProductEntity>

    //DESC
    @Query("SELECT * FROM products ORDER BY CASE WHEN :sortBy = 'price' THEN price WHEN :sortBy = 'name' THEN name WHEN :sortBy = 'stock' THEN stock WHEN :sortBy = 'createdAt' THEN createdAt END DESC LIMIT :limit OFFSET :offset")
    suspend fun getProductsSortedDesc(sortBy: String, limit: Int, offset: Int): List<ProductEntity>


    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchProducts(query: String, offset: Int, limit: Int): List<ProductEntity>

    // Suggestion query
    @Query("""
    SELECT DISTINCT name FROM products
    WHERE name LIKE :query || '%'
    LIMIT 5
""")
    suspend fun getSearchSuggestions(query: String): List<String>


    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): ProductEntity?

    // Nếu cần update stock sau order, thêm:
    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    @Query("""
    SELECT 
        p.*,
        IFNULL(AVG(r.rating), 0) AS avgRating
    FROM products p
    LEFT JOIN product_reviews r 
        ON p.id = r.productId
    WHERE p.name LIKE '%' || :query || '%'
       OR p.description LIKE '%' || :query || '%'
    GROUP BY p.id
    LIMIT :limit OFFSET :offset
""")
    suspend fun searchProductsWithAvgRating(
        query: String,
        limit: Int,
        offset: Int
    ): List<ProductWithAvgRating>

    @Query("""
    SELECT p.*, 
           IFNULL(AVG(r.rating), 0) AS avgRating
    FROM products p
    LEFT JOIN product_reviews r 
        ON p.id = r.productId
    WHERE p.category = :category
    GROUP BY p.id
    LIMIT :limit OFFSET :offset
""")
    suspend fun getProductsByCategoryWithAvgRating(
        category: String,
        limit: Int,
        offset: Int
    ): List<ProductWithAvgRating>

}
