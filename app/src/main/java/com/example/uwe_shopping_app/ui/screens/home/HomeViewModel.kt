package com.example.uwe_shopping_app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class HomeUiState(
    val featuredProducts: List<ProductEntity> = emptyList(),
    val recommendedProducts: List<ProductEntity> = emptyList(),
    val topCollectionProducts: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            try {
                //  Move DB work OFF main thread
                val products = withContext(Dispatchers.IO) {
                    repository.getProductsPaged(offset = 0, limit = 30)
                }

                uiState = uiState.copy(
                    featuredProducts = products.take(6),
                    recommendedProducts = products.drop(6).take(6),
                    topCollectionProducts = products.takeLast(6),
                    isLoading = false
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
