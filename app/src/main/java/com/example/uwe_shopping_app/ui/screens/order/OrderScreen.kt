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
import com.example.uwe_shopping_app.ui.components.order.OrderItem


@Composable
fun OrderScreen(
    navController: NavHostController,
    currentRoute: String,
    initialTab: ShopTab,
    viewModel: OrderViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    val orders by viewModel.orders.collectAsState(initial = emptyList())


//    ORDER ITEMS
    val orderItems = orders.mapNotNull { entity ->
        OrderItem(
            orderId = entity.id ?: return@mapNotNull null,
            subtotal = entity.totalPrice,
            date = entity.createdAt.toString(),
            status = when (entity.status) {
                "pending" -> OrderStatus.ON_THE_WAY
                "delivered" -> OrderStatus.DELIVERED
                "cancelled" -> OrderStatus.CANCELLED
                else -> OrderStatus.ON_THE_WAY
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
                ShopTab.ON_THE_WAY -> OrderStatus.ON_THE_WAY
                ShopTab.DELIVERED -> OrderStatus.DELIVERED
                ShopTab.CANCELLED -> OrderStatus.CANCELLED
                else -> OrderStatus.ON_THE_WAY
            }


            LazyColumn {
                items(
                    orderItems.filter { order ->
                        when (orderStatus) {
                            OrderStatus.ON_THE_WAY -> order.status == OrderStatus.ON_THE_WAY
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
