package com.example.uwe_shopping_app.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
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

class ProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO replace with repository call
                val product = ProductEntity(
                    id = productId,
                    name = "Sportwear Set",
                    description = "Sportwear is fashion today.",
                    price = 80.00,
                    imageResId = android.R.drawable.ic_menu_gallery,
                    stock = 10,
                    category = "Clothing"
                )

                _uiState.value = _uiState.value.copy(
                    product = product,
                    isLoading = false
                )
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

    fun setCurrentImageIndex(index: Int) {
        _uiState.value = _uiState.value.copy(currentImageIndex = index)
    }

    fun toggleDescriptionExpanded() {
        _uiState.value = _uiState.value.copy(
            isDescriptionExpanded = !_uiState.value.isDescriptionExpanded
        )
    }
}
