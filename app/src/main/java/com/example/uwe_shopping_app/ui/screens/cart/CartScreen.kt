package com.example.uwe_shopping_app.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.TopAppBar
import com.example.uwe_shopping_app.ui.components.order.OrderCard
import com.example.uwe_shopping_app.ui.components.order.OrderItem
import com.example.uwe_shopping_app.ui.components.order.OrderStatus
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import androidx.navigation.NavHostController

@Composable
fun CartScreen(
    navController: NavHostController,
    currentRoute: String,
    onNavigate: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(OrderStatus.PENDING) }

    val allOrders = listOf(
        OrderItem("1524", "IK287368838", 2, 110.0, "13/05/2021", OrderStatus.PENDING),
        OrderItem("1525", "IK2873218897", 3, 230.0, "12/05/2021", OrderStatus.PENDING),
        OrderItem("1514", "IK987362534", 2, 110.0, "13/05/2021", OrderStatus.DELIVERED),
        OrderItem("1679", "IK3873218890", 3, 450.0, "12/05/2021", OrderStatus.DELIVERED),
        OrderItem("1671", "IK237368881", 3, 400.0, "10/05/2021", OrderStatus.DELIVERED),
        OrderItem("1200", "IK11112222", 1, 50.0, "09/05/2021", OrderStatus.CANCELLED)
    )

    val filteredOrders = allOrders.filter { it.status == selectedTab }

    Scaffold(
        topBar = {
            TopAppBar()
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // ===== Tabs =====
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = Color.White
            ) {
                OrderStatus.entries.forEach { status ->
                    Tab(
                        selected = selectedTab == status,
                        onClick = { selectedTab = status },
                        text = {
                            Text(
                                status.name,
                                fontWeight = if (selectedTab == status) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // ===== Order list =====
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
            ) {
                items(filteredOrders) { order ->
                    OrderCard(order = order, onDetailsClick = {})
                }
            }
        }
    }
}

// ================= Screen Previews ===================

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CartScreenPreview() {
//    Uwe_shopping_appTheme {
//        CartScreen()
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CartScreenDeliveredPreview() {
//    Uwe_shopping_appTheme {
//        var status by remember { mutableStateOf(OrderStatus.DELIVERED) }
//
//        // Manually set delivered tab for preview
//        Column {
//            Text("Delivered", Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
//
//            LazyColumn {
//                items(
//                    listOf(
//                        OrderItem("1514", "IK987362534", 2, 110.0, "13/05/2021", OrderStatus.DELIVERED),
//                        OrderItem("1679", "IK3873218890", 3, 450.0, "12/05/2021", OrderStatus.DELIVERED)
//                    )
//                ) {
//                    OrderCard(order = it, onDetailsClick = {})
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CartScreenCancelledPreview() {
//    Uwe_shopping_appTheme {
//        LazyColumn {
//            items(
//                listOf(
//                    OrderItem("1200", "IK11112222", 1, 50.0, "09/05/2021", OrderStatus.CANCELLED)
//                )
//            ) {
//                OrderCard(order = it, onDetailsClick = {})
//            }
//        }
//    }
//}