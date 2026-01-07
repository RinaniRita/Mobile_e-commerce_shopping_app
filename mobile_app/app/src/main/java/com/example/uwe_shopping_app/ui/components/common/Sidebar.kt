package com.example.uwe_shopping_app.ui.components.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.ui.screens.profile.ProfileViewModel

sealed class SidebarItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
) {
    object Homepage : SidebarItem("Homepage", Icons.Default.Home, "home")
    object Discover : SidebarItem("Discover", Icons.Default.Search, "search")
    object MyOrder : SidebarItem("My Order", Icons.Default.ShoppingBag, "cart")
    object MyProfile : SidebarItem("My profile", Icons.Default.Person, "profile")
    object Setting : SidebarItem("Setting", Icons.Default.Settings, "setting")
    object Support : SidebarItem("Support", Icons.Default.Email, "chat")
    object AboutUs : SidebarItem("About us", Icons.Default.Info, "about_us")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sidebar(
    isOpen: Boolean,
    onClose: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.user.collectAsState()

    val isGuest = user == null

    // Get the actual current route from navController
    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home"

    // Animate sidebar visibility
    val sidebarAlpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "sidebarAlpha"
    )

    val contentScale by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0.95f,
        animationSpec = tween(durationMillis = 300),
        label = "contentScale"
    )

    if (isOpen || sidebarAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f)
        ) {
            // Backdrop overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f * sidebarAlpha))
                    .clickable { onClose() }
            )

            // Sidebar content - positioned at the start (left)
            Surface(
                modifier = modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(280.dp)
                    .scale(contentScale),
                color = Color.White,
                shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                shadowElevation = 8.dp
            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // User Profile Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (currentRoute != "profile") {
                                navController.navigate("profile") {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo("home") {
                                        saveState = true
                                    }
                                }
                            }
                            onClose()
                        }
                        .padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_placeholder),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Name and Email
                    Column {
                        Text(
                            text = if (isGuest) "Guest" else user!!.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isGuest)
                                "Sign in to access your account"
                            else
                                user!!.email,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp
                            ),
                            color = Color(0xFF808080)
                        )
                    }
                }

                // Main Navigation Items
                val mainItems = listOf(
                    SidebarItem.Homepage,
                    SidebarItem.Discover,
                    SidebarItem.MyOrder,
                    SidebarItem.MyProfile
                )

                mainItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (item.route != currentRoute) {
                                    // Check if we're currently on a sub-route
                                    val actualCurrentRoute = navController.currentBackStackEntry?.destination?.route ?: currentRoute
                                    val isOnSubRoute = actualCurrentRoute !in listOf("home", "search", "cart", "profile")
                                    
                                    if (item.route == "home") {
                                        // For home, always clear the stack
                                        navController.navigate("home") {
                                            launchSingleTop = true
                                            popUpTo("home") {
                                                inclusive = true
                                            }
                                        }
                                    } else if (item.route in listOf("search", "cart", "profile")) {
                                        // For main routes, clear sub-routes if we're on one
                                        if (isOnSubRoute) {
                                            navController.navigate(item.route) {
                                                launchSingleTop = true
                                                // Pop back to home to clear sub-routes
                                                popUpTo("home") {
                                                    inclusive = false
                                                }
                                            }
                                        } else {
                                            // Already on a main route, navigate normally
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
                                onClose()
                            }
                            .padding(vertical = 12.dp, horizontal = 12.dp)
                            .background(
                                color = if (isSelected) Color(0xFFF5F5F5) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) Color.Black else Color(0xFF808080),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 16.sp
                            ),
                            color = if (isSelected) Color.Black else Color(0xFF808080)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "OTHER" Section Label
                Text(
                    text = "OTHER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF808080),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                // Other Navigation Items
                val otherItems = listOf(
                    SidebarItem.Setting,
                    SidebarItem.Support,
                    SidebarItem.AboutUs
                )

                otherItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (item.route != currentRoute) {
                                    // For sub-screens (setting, support, about_us), navigate normally
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                    }
                                }
                                onClose()
                            }
                            .padding(vertical = 12.dp, horizontal = 12.dp)
                            .background(
                                color = if (isSelected) Color(0xFFF5F5F5) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) Color.Black else Color(0xFF808080),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 16.sp
                            ),
                            color = if (isSelected) Color.Black else Color(0xFF808080)
                        )
                    }
                }
            }
            }
        }
    }
}

