package com.example.uwe_shopping_app.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.ui.screens.notification.NotificationViewModel
import kotlinx.coroutines.flow.flowOf


@Composable
fun TopAppBar(
    onMenuClick: () -> Unit = {},
    title: String = "SiuStore",
    modifier: Modifier = Modifier,
    navController: NavController,
    isLoggedIn: Boolean,
    unreadCount: Int,
    notificationsEnabled: Boolean,
    userId: Int?,
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    val userId by sessionManager.userId.collectAsState(initial = null)
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hamburger menu icon
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }

            // App name in center
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Notification bell icon with badge
            val notificationViewModel: NotificationViewModel = viewModel()

            val unreadCount by remember(userId) {
                userId?.let { notificationViewModel.getUnreadCount(it) }
                    ?: flowOf(0)
            }.collectAsState(initial = 0)

            BadgedBox(
                badge = {
                    if (isLoggedIn && notificationsEnabled && unreadCount > 0) {
                        Badge(containerColor = Color.Red)
                    }
                }
            ) {
                IconButton(
                    onClick = {
                        when {
                            !isLoggedIn || userId == null -> {
                                navController.navigate("profile")
                            }
                            else -> {
                                navController.navigate("notification/$userId")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }
        }
    }
}
