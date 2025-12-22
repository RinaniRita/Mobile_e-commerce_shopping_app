package com.example.uwe_shopping_app.ui.screens.product

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import java.util.Locale

@Composable
fun ProductScreen(
    productId: Int,
    onBack: () -> Unit = {},
    // ViewModel sẽ tự động được inject (AndroidViewModel)
    viewModel: ProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current // Lấy context để hiển thị Toast

    // Load sản phẩm khi vào màn hình
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    // --- LẮNG NGHE SỰ KIỆN THÊM THÀNH CÔNG ---
    LaunchedEffect(uiState.isAddToCartSuccess) {
        if (uiState.isAddToCartSuccess) {
            Toast.makeText(context, "Added to cart successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    // Lắng nghe lỗi (ví dụ: chưa login, lỗi DB...)
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(containerColor = Color(0xFFF5F5F5)) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ProductTopBar(
                isFavorite = uiState.isFavorite,
                onFavoriteClick = viewModel::toggleFavorite,
                onBackClick = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                uiState.product?.let { product ->
                    ProductDetailsCard(
                        product = product,
                        onToggleDescription = viewModel::toggleDescriptionExpanded,
                        // --- TRUYỀN HÀM ADD TO CART XUỐNG DƯỚI ---
                        onAddToCart = viewModel::addToCart
                    )
                } ?: run {
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
                                color = Color.Gray
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
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Icon(
                imageVector = if (isFavorite)
                    Icons.Default.Favorite
                else
                    Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else Color.Black
            )
        }
    }
}

@Composable
private fun ProductDetailsCard(
    product: ProductEntity,
    onToggleDescription: () -> Unit,
    // --- THAM SỐ MỚI ĐỂ NHẬN SỰ KIỆN CLICK ---
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Image
            Image(
                painter = painterResource(
                    id = product.imageResId.takeIf { it != 0 }
                        ?: android.R.drawable.ic_menu_gallery
                ),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name + Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format(Locale.getDefault(), "%.2f", product.price)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = product.description,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                // --- GỌI HÀM KHI CLICK ---
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Add to Cart",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    Uwe_shopping_appTheme {
        ProductDetailsCard(
            product = ProductEntity(
                id = 1,
                name = "Sportwear Set",
                description = "Comfortable and modern sportswear.",
                price = 80.00,
                imageResId = android.R.drawable.ic_menu_gallery,
                stock = 10,
                category = "Clothing"
            ),
            onToggleDescription = {},
            onAddToCart = {} // Dummy function cho Preview
        )
    }
}