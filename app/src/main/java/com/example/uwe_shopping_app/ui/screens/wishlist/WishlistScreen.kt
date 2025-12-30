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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.Sidebar
import com.example.uwe_shopping_app.ui.components.common.TopAppBar
import com.example.uwe_shopping_app.ui.components.wishlist.WishlistProduct
import com.example.uwe_shopping_app.ui.components.wishlist.WishlistProductCard
import com.example.uwe_shopping_app.ui.screens.notification.NotificationViewModel
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun WishlistScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isSidebarOpen by remember { mutableStateOf(false) }

    val viewModel: WishlistViewModel = viewModel()
    val products by viewModel.products.collectAsState()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val notificationViewModel: NotificationViewModel = viewModel()

    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    val notificationsEnabled by sessionManager.notificationsEnabled.collectAsState(initial = true)
    val userId by sessionManager.userId.collectAsState(initial = null)

    val unreadCount by remember(userId) {
        userId?.let { notificationViewModel.getUnreadCount(it) }
            ?: flowOf(0)
    }.collectAsState(initial = 0)

    LaunchedEffect(Unit) {
        viewModel.loadWishlist()
    }


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
                navController = navController,
                onMenuClick = { isSidebarOpen = true },
                isLoggedIn = isLoggedIn,
                notificationsEnabled = notificationsEnabled,
                unreadCount = unreadCount,
                userId = userId
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
                items(products, key = { it.id }) { product ->
                    WishlistProductCard(
                        product = product,
                        onClick = {
                            navController.navigate("product/${product.id}")
                        },
                        onFavoriteClick = {
                            viewModel.remove(product.id)
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

