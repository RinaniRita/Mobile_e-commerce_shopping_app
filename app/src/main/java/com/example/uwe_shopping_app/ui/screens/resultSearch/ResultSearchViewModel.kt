package com.example.uwe_shopping_app.ui.screens.resultSearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.data.local.entity.ProductEntity

data class ResultSearchUiState(
    val query: String = "",
    val searchResults: List<ProductEntity> = emptyList()
)

class ResultSearchViewModel : ViewModel() {

    // Local mock catalog used only for result searching
    private val allProducts: List<ProductEntity> = listOf(
        ProductEntity(
            id = 1,
            name = "Long Sleeve Dress",
            description = "Elegant long sleeve dress",
            price = 45.00,
            category = "Clothing",
            stock = 10,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 2,
            name = "Sportwear Set",
            description = "Comfortable sportswear set",
            price = 80.00,
            category = "Sportswear",
            stock = 8,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 3,
            name = "Sweater",
            description = "Warm winter sweater",
            price = 35.00,
            category = "Clothing",
            stock = 15,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 4,
            name = "White Hoodie",
            description = "Casual white hoodie",
            price = 29.00,
            category = "Hoodies",
            stock = 12,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 5,
            name = "Cotton T-Shirt",
            description = "Soft cotton t-shirt",
            price = 30.00,
            category = "T-Shirts",
            stock = 20,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 6,
            name = "Casual Shirt",
            description = "Everyday casual shirt",
            price = 32.00,
            category = "Shirts",
            stock = 14,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 7,
            name = "Office Life T-Shirt",
            description = "Smart casual office t-shirt",
            price = 25.00,
            category = "Office Wear",
            stock = 18,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 8,
            name = "Elegant Dress",
            description = "Elegant evening dress",
            price = 65.00,
            category = "Dresses",
            stock = 6,
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        ProductEntity(
            id = 9,
            name = "Summer Collection",
            description = "Light summer clothing",
            price = 55.00,
            category = "Seasonal",
            stock = 9,
            imageResId = android.R.drawable.ic_menu_gallery
        )
    )

    var uiState by mutableStateOf(ResultSearchUiState())
        private set

    fun search(query: String) {
        val trimmedQuery = query.trim()

        val filteredProducts = if (trimmedQuery.isBlank()) {
            emptyList()
        } else {
            allProducts.filter { product ->
                product.name.contains(trimmedQuery, ignoreCase = true) ||
                        product.description.contains(trimmedQuery, ignoreCase = true) ||
                        product.category.contains(trimmedQuery, ignoreCase = true)
            }
        }

        uiState = ResultSearchUiState(
            query = trimmedQuery,
            searchResults = filteredProducts
        )
    }
}
