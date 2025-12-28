package com.example.uwe_shopping_app.ui.screens.resultSearch

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.model.ProductWithAvgRating
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import com.example.uwe_shopping_app.ui.components.search.SearchFilterState
import com.example.uwe_shopping_app.ui.components.search.SortOption
import kotlinx.coroutines.launch

/**
 * UI state for the Result Search screen.
 * Holds everything the UI needs to render.
 */
data class ResultSearchUiState(
    val query: String = "",
    val searchResults: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val filterState: SearchFilterState = SearchFilterState(),
    val showFilter: Boolean = false
)

private fun isCategory(query: String): Boolean {
    return query in listOf(
        "Electronics",
        "Fashion",
        "Home",
        "Beauty",
        "Sports"
    )
}

/**
 * ViewModel responsible for:
 * - Performing product searches
 * - Applying price filters
 * - Applying sorting options
 * - Exposing state to the UI via Compose state
 */
class ResultSearchViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ResultSearchUiState())
        private set

    fun setQuery(query: String) {
        uiState = uiState.copy(query = query)
        search()
    }

    fun updateFilterState(newState: SearchFilterState) {
        uiState = uiState.copy(filterState = newState)
        search()
    }

    fun resetFilter() {
        uiState = uiState.copy(filterState = SearchFilterState())
        search()
    }

    fun showFilter() {
        uiState = uiState.copy(showFilter = true)
    }

    fun hideFilter() {
        uiState = uiState.copy(showFilter = false)
    }

    fun applyFilter() {
        uiState = uiState.copy(showFilter = false)
        search()
    }

    private fun search() {
        val query = uiState.query.trim()
        val filter = uiState.filterState

        if (query.isBlank()) {
            uiState = uiState.copy(searchResults = emptyList())
            return
        }

        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val rawResults = if (isCategory(query)) {
                repository.getProductsByCategoryWithAvgRating(query, 0)
            } else {
                repository.searchProductsWithAvgRating(query, 0, 50)
            }

            val filtered = rawResults.filter {
                it.product.price in filter.minPrice..filter.maxPrice
            }

            val sorted = when (filter.sortBy) {
                SortOption.NEWEST ->
                    filtered.sortedByDescending { it.product.createdAt }

                SortOption.OLDEST ->
                    filtered.sortedBy { it.product.createdAt }

                SortOption.NAME_ASC ->
                    filtered.sortedBy { it.product.name }

                SortOption.NAME_DESC ->
                    filtered.sortedByDescending { it.product.name }

                SortOption.RATING_ASC ->
                    filtered.sortedBy { it.avgRating }

                SortOption.RATING_DESC ->
                    filtered.sortedByDescending { it.avgRating }
            }

            uiState = uiState.copy(
                searchResults = sorted.map { it.product },
                isLoading = false
            )
        }
    }
}

