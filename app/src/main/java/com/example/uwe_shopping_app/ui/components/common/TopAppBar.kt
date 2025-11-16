package com.example.uwe_shopping_app.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun TopAppBar(
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    hasNotifications: Boolean = true,
    modifier: Modifier = Modifier
) {
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
                text = "SiuStore",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Notification bell icon with badge
            Box {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Black
                    )
                }
                if (hasNotifications) {
                    // Notification dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(8.dp)
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = Color(0xFFFF3B30) // Red notification dot
                        ) {}
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopAppBarPreview() {
    Uwe_shopping_appTheme {
        TopAppBar(
            onMenuClick = {},
            onNotificationClick = {},
            hasNotifications = true
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopAppBarNoNotificationsPreview() {
    Uwe_shopping_appTheme {
        TopAppBar(
            onMenuClick = {},
            onNotificationClick = {},
            hasNotifications = false
        )
    }
}