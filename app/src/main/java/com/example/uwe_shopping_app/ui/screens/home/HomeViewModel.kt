package com.example.uwe_shopping_app.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.domain.model.Product

data class HomeUiState(
    val featuredProducts: List<Product> = emptyList(),
    val recommendedProducts: List<Product> = emptyList(),
    val topCollectionProducts: List<Product> = emptyList()
)

class HomeViewModel : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadMockData()
    }

    private fun loadMockData() {
        val featured = listOf(
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
            )
        )

        val recommended = listOf(
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
            )
        )

        val topCollection = listOf(
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

        uiState = HomeUiState(
            featuredProducts = featured,
            recommendedProducts = recommended,
            topCollectionProducts = topCollection
        )
    }
}
