package com.example.uwe_shopping_app.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import kotlinx.coroutines.launch

data class SearchUiState(
    val searchQuery: String = "",
    val suggestions: List<String> = emptyList(),
    val products: List<ProductEntity> = emptyList()
)


class SearchViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(searchQuery = query)

        if (query.length >= 2) {
            loadSuggestions(query)
        } else {
            uiState = uiState.copy(suggestions = emptyList())
        }
    }

    private fun loadSuggestions(query: String) {
        viewModelScope.launch {
            val suggestions = repository.getSearchSuggestions(query)
            uiState = uiState.copy(suggestions = suggestions)
        }
    }

    fun applyFilter(
        minPrice: Float,
        maxPrice: Float,
        sortBy: SortOption
    ) {
        viewModelScope.launch {

            val (sortField, sortOrder) = when (sortBy) {
                SortOption.NEWEST -> "createdAt" to "DESC"
                SortOption.OLDEST -> "createdAt" to "ASC"
                SortOption.NAME_ASC -> "name" to "ASC"
                SortOption.NAME_DESC -> "name" to "DESC"
            }

            val products = repository.getProductsSorted(
                sortBy = sortField,
                sortOrder = sortOrder,
                offset = 0
            ).filter {
                it.price in minPrice..maxPrice
            }

            uiState = uiState.copy(products = products)
        }
    }



    fun submitSearch() {
        uiState = uiState.copy(suggestions = emptyList())
    }

    fun clearSearch() {
        uiState = SearchUiState()
    }
}

