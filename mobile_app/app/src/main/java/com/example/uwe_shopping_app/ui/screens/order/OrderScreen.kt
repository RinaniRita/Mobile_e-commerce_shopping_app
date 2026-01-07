package com.example.uwe_shopping_app.ui.screens.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.cart.CartHeader
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.ShopTab
import com.example.uwe_shopping_app.ui.components.order.OrderCard
import com.example.uwe_shopping_app.ui.components.order.OrderStatus
import com.example.uwe_shopping_app.ui.components.common.ShopStatusTabs


@Composable
fun OrderScreen(
    navController: NavHostController,
    currentRoute: String,
    initialTab: ShopTab,
    viewModel: OrderViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    val orderItems by viewModel.orderItems.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)

    Scaffold(
        topBar = {
            CartHeader(onBackClick = { navController.popBackStack() })
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { padding ->

        Column(Modifier.padding(padding)) {

            // ---------------- TABS (VISIBLE FOR BOTH) ----------------
            ShopStatusTabs(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        ShopTab.YOUR_CART -> navController.popBackStack()
                        else ->
                            navController.navigate("orders?status=${tab.name}") {
                                launchSingleTop = true
                            }
                    }
                }
            )

            // ===================== NOT LOGGED IN =====================
            if (!isLoggedIn) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Please log in to view your orders",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Track your deliveries and order history",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Button(
                            onClick = { navController.navigate("login") }
                        ) {
                            Text("Log in / Sign up")
                        }
                    }
                }

                return@Scaffold
            }

            // ===================== LOGGED IN =====================
            val orderStatus = when (selectedTab) {
                ShopTab.ON_THE_WAY -> OrderStatus.ON_THE_WAY
                ShopTab.DELIVERED -> OrderStatus.DELIVERED
                ShopTab.CANCELLED -> OrderStatus.CANCELLED
                else -> OrderStatus.ON_THE_WAY
            }

            LazyColumn {
                items(
                    orderItems.filter { order ->
                        order.status == orderStatus
                    }
                ) { order ->
                    OrderCard(
                        order = order,
                        onDetailsClick = {
                            navController.navigate("orderInfo/${order.orderId}")
                        },
                        onConfirmDelivered = {
                            viewModel.markAsDelivered(order.orderId)
                        },
                        onCancelOrder = {
                            viewModel.cancelOrder(order.orderId)
                        }
                    )
                }
            }
        }
    }
}

