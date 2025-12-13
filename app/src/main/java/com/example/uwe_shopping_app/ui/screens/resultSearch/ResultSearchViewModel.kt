package com.example.uwe_shopping_app.ui.screens.resultSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

data class ResultSearchUiState(
    val query: String = "",
    val searchResults: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false
)

class ResultSearchViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ResultSearchUiState())
        private set

    fun search(query: String) {
        val trimmedQuery = query.trim()

        if (trimmedQuery.isBlank()) {
            uiState = ResultSearchUiState()
            return
        }

        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val results = repository.searchProducts(
                query = trimmedQuery,
                offset = 0,
                limit = 50
            )

            uiState = ResultSearchUiState(
                query = trimmedQuery,
                searchResults = results,
                isLoading = false
            )
        }
    }
}
