package com.example.uwe_shopping_app.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.domain.model.Product
import com.example.uwe_shopping_app.domain.model.ProductColor
import com.example.uwe_shopping_app.ui.components.product.*
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import java.util.Locale

@Composable
fun ProductScreen(
    productId: String,
    onBack: () -> Unit = {},
    viewModel: ProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top App Bar with Back and Favorite buttons
            ProductTopBar(
                isFavorite = uiState.isFavorite,
                onFavoriteClick = { viewModel.toggleFavorite() },
                onBackClick = onBack
            )

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Image Carousel
                uiState.product?.let { product ->
                    ImageCarousel(
                        product = product,
                        currentImageIndex = uiState.currentImageIndex,
                        onImageIndexChanged = { viewModel.setCurrentImageIndex(it) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Product Details Card
                    ProductDetailsCard(
                        product = product,
                        uiState = uiState,
                        onColorSelected = { viewModel.selectColor(it) },
                        onSizeSelected = { viewModel.selectSize(it) },
                        onToggleDescription = { viewModel.toggleDescriptionExpanded() }
                    )
                } ?: run {
                    // Loading or error state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = uiState.error ?: "Product not found",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductTopBar(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Favorite button
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color(0xFFFF3B30) else Color.Black
                )
            }
        }
    }
}

@Composable
private fun ProductDetailsCard(
    product: com.example.uwe_shopping_app.domain.model.Product,
    uiState: ProductUiState,
    onColorSelected: (Int) -> Unit,
    onSizeSelected: (Int) -> Unit,
    onToggleDescription: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Product Name and Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$${String.format(Locale.getDefault(), "%.2f", product.price)}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rating
            RatingDisplay(
                rating = product.rating,
                reviewCount = product.reviewCount,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Color and Size Selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (product.availableColors.isNotEmpty()) {
                    ColorSelector(
                        colors = product.availableColors,
                        selectedIndex = uiState.selectedColorIndex,
                        onColorSelected = onColorSelected,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                if (product.availableSizes.isNotEmpty()) {
                    SizeSelector(
                        sizes = product.availableSizes,
                        selectedIndex = uiState.selectedSizeIndex,
                        onSizeSelected = onSizeSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Description
            product.description?.let { description ->
                ExpandableDescription(
                    description = description,
                    isExpanded = uiState.isDescriptionExpanded,
                    onToggleExpanded = onToggleDescription
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add to Cart Button (placeholder for future implementation)
            Button(
                onClick = { /* TODO: Add to cart */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text(
                    text = "Add to Cart",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductScreenPreview() {
    Uwe_shopping_appTheme {
        // Create mock product data for preview
        val mockProduct = Product(
            id = "1",
            name = "Sportwear Set",
            price = 80.00,
            description = "Sportswear is no longer under culture, it is no longer indie or cobbled together as it once was. Sport is fashion today. The top is oversized in fit and style, may need to size down.",
            rating = 4.5,
            reviewCount = 83,
            availableColors = listOf(
                ProductColor("Beige", 0xFFF5E6D3),
                ProductColor("Black", 0xFF000000),
                ProductColor("Coral", 0xFFFF6B6B)
            ),
            availableSizes = listOf("S", "M", "L"),
            imageResIds = listOf(android.R.drawable.ic_menu_gallery)
        )
        
        val mockUiState = ProductUiState(
            product = mockProduct,
            isLoading = false,
            selectedColorIndex = 0,
            selectedSizeIndex = 2, // "L" selected
            isFavorite = false,
            currentImageIndex = 0,
            isDescriptionExpanded = false
        )
        
        Scaffold(
            containerColor = Color(0xFFF5F5F5)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Top App Bar with Back and Favorite buttons
                ProductTopBar(
                    isFavorite = mockUiState.isFavorite,
                    onFavoriteClick = { },
                    onBackClick = { }
                )

                // Main content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Image Carousel
                    ImageCarousel(
                        product = mockProduct,
                        currentImageIndex = mockUiState.currentImageIndex,
                        onImageIndexChanged = { },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Product Details Card
                    ProductDetailsCard(
                        product = mockProduct,
                        uiState = mockUiState,
                        onColorSelected = { },
                        onSizeSelected = { },
                        onToggleDescription = { }
                    )
                }
            }
        }
    }
}
