package com.example.uwe_shopping_app.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Outlined.Home)
    object Search : BottomNavItem("search", "Search", Icons.Outlined.Search)
    object Cart : BottomNavItem("cart", "Cart", Icons.Outlined.ShoppingCart)
    object Profile : BottomNavItem("profile", "Profile", Icons.Outlined.Person)
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )
    
    // Get actual current route from navController
    val actualCurrentRoute = navController.currentBackStackEntry?.destination?.route ?: currentRoute
    // Check if we're on a sub-route (like wishlist, voucher, etc.)
    val isOnSubRoute = actualCurrentRoute !in items.map { it.route }
    // Determine which main route to show as selected
    val mainRouteForSelection = when {
        actualCurrentRoute == "wishlist" -> "profile"
        actualCurrentRoute in items.map { it.route } -> actualCurrentRoute
        else -> currentRoute
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                // Show as selected if this is the main route for selection
                val isSelected = mainRouteForSelection == item.route

                IconButton(
                    onClick = {
                        // Navigate to the main route
                        when (item.route) {
                            "home" -> {
                                // For home, always clear the entire stack
                                navController.navigate("home") {
                                    launchSingleTop = true
                                    popUpTo("home") {
                                        inclusive = true
                                    }
                                }
                            }
                            "search", "cart", "profile" -> {
                                // For other main routes
                                if (isOnSubRoute) {
                                    // If we're on a sub-route (like wishlist), clear it first
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        // Pop back to the main route stack, clearing sub-routes
                                        popUpTo("home") {
                                            inclusive = false
                                        }
                                    }
                                } else {
                                    // Already on a main route, navigate with state saving
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo("home") {
                                            saveState = true
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) Color.Black else Color(0xFF9E9E9E),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

