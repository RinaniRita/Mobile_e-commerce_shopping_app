package com.example.uwe_shopping_app.ui.screens.resultSearch

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import com.example.uwe_shopping_app.ui.screens.search.SortOption
import kotlinx.coroutines.launch

/**
 * UI state for the Result Search screen.
 * Holds everything the UI needs to render.
 */
data class ResultSearchUiState(
    val query: String = "",
    val searchResults: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false
)

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

    /**
     * Current UI state observed by the ResultSearchScreen.
     * Using mutableStateOf so Compose automatically recomposes on changes.
     */
    var uiState by mutableStateOf(ResultSearchUiState())
        private set

    /**
     * Stores the last searched query.
     * Useful if you want to avoid duplicate searches or re-run the same query.
     */
    private var lastQuery: String = ""

    /**
     * Perform a product search with filtering and sorting.
     *
     * @param query     Search keyword entered by the user
     * @param minPrice  Minimum price filter
     * @param maxPrice  Maximum price filter
     * @param sortBy    Selected sorting option
     */
    fun search(
        query: String,
        minPrice: Float,
        maxPrice: Float,
        sortBy: SortOption
    ) {
        // Clean up user input
        val trimmedQuery = query.trim()
        lastQuery = trimmedQuery

        // If query is empty, reset state and stop
        if (trimmedQuery.isBlank()) {
            uiState = ResultSearchUiState()
            return
        }

        // Notify UI that loading has started
        uiState = uiState.copy(isLoading = true)

        // Launch database work on a background coroutine
        viewModelScope.launch {

            // Fetch raw results from the database
            val dbResults = repository.searchProducts(
                query = trimmedQuery,
                offset = 0,
                limit = 50
            )

            // Apply price range filtering
            val filteredResults = dbResults.filter {
                it.price in minPrice.toDouble()..maxPrice.toDouble()
            }

            // Apply sorting based on selected option
            val sortedResults = when (sortBy) {
                SortOption.NEWEST ->
                    filteredResults.sortedByDescending { it.createdAt }

                SortOption.OLDEST ->
                    filteredResults.sortedBy { it.createdAt }

                SortOption.NAME_ASC ->
                    filteredResults.sortedBy { it.name }

                SortOption.NAME_DESC ->
                    filteredResults.sortedByDescending { it.name }
            }

            // Update UI state with final results
            uiState = ResultSearchUiState(
                query = trimmedQuery,
                searchResults = sortedResults,
                isLoading = false
            )
        }
    }
}
