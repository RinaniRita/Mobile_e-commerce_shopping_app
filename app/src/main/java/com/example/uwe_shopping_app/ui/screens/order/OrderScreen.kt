package com.example.uwe_shopping_app.ui.screens.order


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    val orders by viewModel.orders.collectAsState()

//    ORDER ITEMS
    val orderItems = orders.map { entity ->
        com.example.uwe_shopping_app.ui.components.order.OrderItem(
            orderId = entity.id.toString(),
            subtotal = entity.totalPrice,
            date = entity.createdAt.toString(), // format later
            status = when (entity.status) {
                "pending" -> OrderStatus.PENDING
                "delivered" -> OrderStatus.DELIVERED
                "cancelled" -> OrderStatus.CANCELLED
                else -> OrderStatus.PENDING
            }
        )
    }


    Scaffold(
        topBar = { CartHeader(onBackClick = { navController.popBackStack() }) },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {

            ShopStatusTabs(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab

                    when (tab) {
                        ShopTab.YOUR_CART ->
                            navController.popBackStack()

                        else ->
                            navController.navigate("orders?status=${tab.name}") {
                                launchSingleTop = true
                            }
                    }
                }
            )

//            ORDER STATUS
            val orderStatus = when (selectedTab) {
                ShopTab.ON_THE_WAY -> OrderStatus.PENDING
                ShopTab.DELIVERED -> OrderStatus.DELIVERED
                ShopTab.CANCELLED -> OrderStatus.CANCELLED
                else -> OrderStatus.PENDING
            }


            LazyColumn {
                items(
                    orderItems.filter { order ->
                        when (orderStatus) {
                            OrderStatus.PENDING -> order.status == OrderStatus.PENDING
                            OrderStatus.DELIVERED -> order.status == OrderStatus.DELIVERED
                            OrderStatus.CANCELLED -> order.status == OrderStatus.CANCELLED
                            else -> false
                        }
                    }
                ) { order ->
                    OrderCard(
                        order = order,
                        onDetailsClick = {
                            navController.navigate("orderInfo/${order.orderId}")
                        }
                    )
                }
            }
        }
    }
}
