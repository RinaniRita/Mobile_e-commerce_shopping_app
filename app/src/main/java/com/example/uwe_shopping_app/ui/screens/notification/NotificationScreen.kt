package com.example.uwe_shopping_app.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uwe_shopping_app.data.local.entity.NotificationEntity
import com.example.uwe_shopping_app.ui.components.common.ShopTab


data class NotificationUi(
    val title: String,
    val message: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    userId: Int,
    onBack: () -> Unit,
    viewModel: NotificationViewModel = viewModel(),
    navController: NavController
) {
    val notifications by viewModel
        .getNotifications(userId)
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Notification", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (notifications.any { !it.isRead }) {
                        TextButton(
                            onClick = { viewModel.markAllAsRead(userId) }
                        ) {
                            Text("Mark all")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(
                    notification = notification,
                    onClick = {
                        if (!notification.isRead) {
                            viewModel.markAsRead(notification.id)
                        }

                        when (notification.type) {
                            "VOUCHER" -> {
                                navController.navigate("voucher")
                            }

                            "SHIPPING" -> {
                                // Go to Orders → On the way
                                navController.navigate(
                                    "orders?status=${ShopTab.ON_THE_WAY.name}"
                                )
                            }

                            "ORDER" -> {
                                notification.referenceId?.let {
                                    navController.navigate("orderInfo/$it")
                                }
                            }
                        }
                    }
                )

            }
        }
    }
}


@Composable
fun NotificationCard(
    notification: NotificationEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (notification.isRead) Color.White
                else Color(0xFFF1F6FF) // unread highlight
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = notification.message,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            if (!notification.isRead) {
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Red, shape = CircleShape)
                )
            }
        }
    }
}
