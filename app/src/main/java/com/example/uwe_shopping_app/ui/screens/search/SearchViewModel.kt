package com.example.uwe_shopping_app.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.domain.model.Product

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList()
)

class SearchViewModel : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    init {
        loadMockData()
    }

    private fun loadMockData() {
        // Load all products for search
        val allProducts = listOf(
            Product(
                id = "1",
                name = "Long Sleeve Dress",
                price = 45.00
            ),
            Product(
                id = "2",
                name = "Sportwear Set",
                price = 80.00
            ),
            Product(
                id = "3",
                name = "Sweater",
                price = 35.00
            ),
            Product(
                id = "4",
                name = "White Hoodie",
                price = 29.00
            ),
            Product(
                id = "5",
                name = "Cotton T‑Shirt",
                price = 30.00
            ),
            Product(
                id = "6",
                name = "Casual Shirt",
                price = 32.00
            ),
            Product(
                id = "7",
                name = "Office Life T‑Shirt",
                price = 25.00
            ),
            Product(
                id = "8",
                name = "Elegant Dress",
                price = 65.00
            ),
            Product(
                id = "9",
                name = "Summer Collection",
                price = 55.00
            )
        )

        uiState = SearchUiState(
            allProducts = allProducts
        )
    }

    fun updateSearchQuery(query: String) {
        val filteredProducts = if (query.isBlank()) {
            emptyList()
        } else {
            uiState.allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.description?.contains(query, ignoreCase = true) == true ||
                product.category?.contains(query, ignoreCase = true) == true
            }
        }

        uiState = uiState.copy(
            searchQuery = query,
            searchResults = filteredProducts
        )
    }

    fun clearSearch() {
        uiState = uiState.copy(
            searchQuery = "",
            searchResults = emptyList()
        )
    }
}

