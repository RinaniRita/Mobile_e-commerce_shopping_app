package com.example.uwe_shopping_app.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class SearchUiState(
    val searchQuery: String = ""
)

class SearchViewModel : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(searchQuery = query)
    }

    fun clearSearch() {
        uiState = SearchUiState()
    }
}

