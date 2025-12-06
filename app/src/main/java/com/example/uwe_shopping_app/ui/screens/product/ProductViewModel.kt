package com.example.uwe_shopping_app.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedColorIndex: Int = 0,
    val selectedSizeIndex: Int = 0,
    val isFavorite: Boolean = false,
    val currentImageIndex: Int = 0,
    val isDescriptionExpanded: Boolean = false
)

class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // TODO: Replace with actual repository call
                // For now, using mock data
                val mockProduct = createMockProduct(productId)
                _uiState.value = _uiState.value.copy(
                    product = mockProduct,
                    isLoading = false,
                    selectedColorIndex = 0,
                    selectedSizeIndex = if (mockProduct.availableSizes.isNotEmpty()) {
                        mockProduct.availableSizes.indexOf("L").takeIf { it >= 0 } ?: 0
                    } else 0
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load product"
                )
            }
        }
    }

    fun selectColor(index: Int) {
        _uiState.value = _uiState.value.copy(selectedColorIndex = index)
    }

    fun selectSize(index: Int) {
        _uiState.value = _uiState.value.copy(selectedSizeIndex = index)
    }

    fun toggleFavorite() {
        _uiState.value = _uiState.value.copy(isFavorite = !_uiState.value.isFavorite)
    }

    fun setCurrentImageIndex(index: Int) {
        _uiState.value = _uiState.value.copy(currentImageIndex = index)
    }

    fun toggleDescriptionExpanded() {
        _uiState.value = _uiState.value.copy(
            isDescriptionExpanded = !_uiState.value.isDescriptionExpanded
        )
    }

    // Mock product data - replace with actual repository call
    private fun createMockProduct(productId: String): Product {
        return Product(
            id = productId,
            name = "Sportwear Set",
            price = 80.00,
            description = "Sportswear is no longer under culture, it is no longer indie or cobbled together as it once was. Sport is fashion today. The top is oversized in fit and style, may need to size down.",
            rating = 4.5,
            reviewCount = 83,
            availableColors = listOf(
                com.example.uwe_shopping_app.domain.model.ProductColor("Beige", 0xFFF5E6D3),
                com.example.uwe_shopping_app.domain.model.ProductColor("Black", 0xFF000000),
                com.example.uwe_shopping_app.domain.model.ProductColor("Coral", 0xFFFF6B6B)
            ),
            availableSizes = listOf("S", "M", "L"),
            imageResIds = listOf(
                // Using a placeholder - you can replace with actual drawable resources
                android.R.drawable.ic_menu_gallery
            )
        )
    }
}
