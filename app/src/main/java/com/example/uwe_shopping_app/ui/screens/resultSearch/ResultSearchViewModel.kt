package com.example.uwe_shopping_app.ui.screens.resultSearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.domain.model.Product

data class ResultSearchUiState(
    val query: String = "",
    val searchResults: List<Product> = emptyList()
)

class ResultSearchViewModel : ViewModel() {

    // Local mock catalog used only for result searching
    private val allProducts: List<Product> = listOf(
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

    var uiState by mutableStateOf(ResultSearchUiState())
        private set

    fun search(query: String) {
        val trimmedQuery = query.trim()

        val filteredProducts = if (trimmedQuery.isBlank()) {
            emptyList()
        } else {
            allProducts.filter { product ->
                product.name.contains(trimmedQuery, ignoreCase = true) ||
                    product.description?.contains(trimmedQuery, ignoreCase = true) == true ||
                    product.category?.contains(trimmedQuery, ignoreCase = true) == true
            }
        }

        uiState = ResultSearchUiState(
            query = trimmedQuery,
            searchResults = filteredProducts
        )
    }
}


