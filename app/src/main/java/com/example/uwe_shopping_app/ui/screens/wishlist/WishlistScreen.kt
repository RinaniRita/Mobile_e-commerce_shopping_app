package com.example.uwe_shopping_app.ui.screens.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.Sidebar
import com.example.uwe_shopping_app.ui.components.common.TopAppBar
import com.example.uwe_shopping_app.ui.components.wishlist.WishlistProduct
import com.example.uwe_shopping_app.ui.components.wishlist.WishlistProductCard
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun WishlistScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isSidebarOpen by remember { mutableStateOf(false) }

    // Sample wishlist products matching the image
    val wishlistProducts = listOf(
        WishlistProduct(
            id = 1,
            name = "Front Tie Mini Dress",
            price = 59.00,
            imageResId = R.drawable.welcome_img,
            rating = 4.5,
            reviewCount = 38
        ),
        WishlistProduct(
            id = 2,
            name = "Linen Dress",
            price = 52.00,
            originalPrice = 90.00,
            imageResId = R.drawable.welcome_img,
            rating = 4.5,
            reviewCount = 64
        ),
        WishlistProduct(
            id = 3,
            name = "Ohara Dress",
            price = 85.00,
            imageResId = R.drawable.welcome_img,
            rating = 4.5,
            reviewCount = 50
        ),
        WishlistProduct(
            id = 4,
            name = "Tie Back Mini Dress",
            price = 67.00,
            imageResId = R.drawable.welcome_img,
            rating = 4.5,
            reviewCount = 39
        )
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Top App Bar
            TopAppBar(
                title = "My Wishlist",
                onMenuClick = { isSidebarOpen = true },
                onNotificationClick = {}
            )

            // Product Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                items(wishlistProducts, key = { it.id }) { product ->
                    WishlistProductCard(
                        product = product,
                        onClick = {
                            navController.navigate("product/${product.id}")
                        },
                        onFavoriteClick = {
                            // Handle remove from wishlist (frontend only)
                        }
                    )
                }
            }

            // Bottom Navigation Bar - show profile as selected since wishlist is accessed from profile
            BottomNavigationBar(
                navController = navController,
                currentRoute = "profile"
            )
        }

        // Sidebar
        Sidebar(
            isOpen = isSidebarOpen,
            onClose = { isSidebarOpen = false },
            navController = navController,
            modifier = Modifier.zIndex(10f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WishlistScreenPreview() {
    Uwe_shopping_appTheme {
        WishlistScreen(
            navController = androidx.navigation.compose.rememberNavController()
        )
    }
}

