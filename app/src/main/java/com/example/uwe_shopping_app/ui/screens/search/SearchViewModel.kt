package com.example.uwe_shopping_app.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import com.example.uwe_shopping_app.ui.components.search.SearchFilterState
import com.example.uwe_shopping_app.ui.components.search.SortOption
import kotlinx.coroutines.launch

data class SearchUiState(
    val searchQuery: String = "",
    val suggestions: List<String> = emptyList(),
    val products: List<ProductEntity> = emptyList(),

    val filterState: SearchFilterState = SearchFilterState(),
    val showFilter: Boolean = false
)


class SearchViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    /* ---------------- Category ---------------- */
    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            val products = repository.getProductsByCategory(
                category = category,
                offset = 0
            )

            uiState = uiState.copy(
                products = products
            )
        }
    }


    /* ---------------- Search ---------------- */
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

    fun submitSearch() {
        uiState = uiState.copy(suggestions = emptyList())
    }

    fun clearSearch() {
        uiState = SearchUiState()
    }

    /* ---------------- Filter ---------------- */
    fun showFilter() {
        uiState = uiState.copy(showFilter = true)
    }

    fun hideFilter() {
        uiState = uiState.copy(showFilter = false)
    }

    fun updateFilterState(state: SearchFilterState) {
        uiState = uiState.copy(filterState = state)
    }

    fun resetFilter() {
        uiState = uiState.copy(filterState = SearchFilterState())
    }

    fun applyFilter() {
        val filter = uiState.filterState

        viewModelScope.launch {
            val (sortField, sortOrder) = when (filter.sortBy) {
                SortOption.NEWEST -> "createdAt" to "DESC"
                SortOption.OLDEST -> "createdAt" to "ASC"
                SortOption.NAME_ASC -> "name" to "ASC"
                SortOption.NAME_DESC -> "name" to "DESC"
                SortOption.RATING_ASC,
                SortOption.RATING_DESC -> "createdAt" to "DESC"
            }

            val products = repository.getProductsSorted(
                sortBy = sortField,
                sortOrder = sortOrder,
                offset = 0
            ).filter {
                it.price in filter.minPrice..filter.maxPrice
            }

            uiState = uiState.copy(
                products = products,
                showFilter = false
            )
        }
    }


}

