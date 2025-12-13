package com.example.uwe_shopping_app.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductUiState(
    val product: ProductEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentImageIndex: Int = 0,
    val isFavorite: Boolean = false,
    val isDescriptionExpanded: Boolean = false
)

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val product = repository.getProductById(productId)

                if (product != null) {
                    _uiState.value = _uiState.value.copy(
                        product = product,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Product not found",
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load product",
                    isLoading = false
                )
            }
        }
    }

    fun toggleFavorite() {
        _uiState.value = _uiState.value.copy(
            isFavorite = !_uiState.value.isFavorite
        )
    }

    fun toggleDescriptionExpanded() {
        _uiState.value = _uiState.value.copy(
            isDescriptionExpanded = !_uiState.value.isDescriptionExpanded
        )
    }

}

