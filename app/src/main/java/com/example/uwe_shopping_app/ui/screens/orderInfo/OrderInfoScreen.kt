package com.example.uwe_shopping_app.ui.screens.orderInfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.cart.CartHeader
import com.example.uwe_shopping_app.ui.components.order.OrderStatus

@Composable
fun OrderInfoScreen(
    navController: NavHostController,
    orderId: Int,
    userId: Int,
    viewModel: OrderInfoViewModel = viewModel()
) {
    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    val state = viewModel.uiState


    Scaffold(
        topBar = {
            CartHeader(onBackClick = { navController.popBackStack() })
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // ===== Status banner =====
            StatusBanner(state.status)

            Spacer(Modifier.height(16.dp))

            // ===== Order info =====
            InfoCard {
                InfoRow("Order number", "#${state.orderId}")
                InfoRow("Tracking Number", state.trackingNumber)
                InfoRow("Delivery address", state.deliveryAddress)
            }

            Spacer(Modifier.height(16.dp))

            // ===== Items & rating =====
            InfoCard {
                state.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${item.name} x${item.quantity}")
                            Text(
                                "$${"%.2f".format(item.price)}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Divider()
                }

                Spacer(Modifier.height(8.dp))

                PriceRow("Sub total", state.subtotal)
                PriceRow("Shipping", state.shipping)
                Divider()
                PriceRow("Total", state.subtotal + state.shipping, bold = true)
            }

            Spacer(Modifier.height(24.dp))

            // ===== Bottom action =====
            when (state.status) {
                OrderStatus.DELIVERED -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate("home") }
                        ) {
                            Text("Return home")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val firstProduct = state.items.firstOrNull()
                                if (firstProduct != null) {
                                    navController.navigate("review/${state.orderId}/$userId")

                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text("Rate products", color = Color.White)
                        }
                    }
                }

                else -> {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate("home") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("Return home", color = Color.White)
                    }
                }
            }


            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatusBanner(status: OrderStatus) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF424242)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = when (status) {
                        OrderStatus.ON_THE_WAY -> "Your order is on the way"
                        OrderStatus.DELIVERED -> "Your order has been delivered"
                        OrderStatus.CANCELLED -> "This order was cancelled"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Icon(
                imageVector = when (status) {
                    OrderStatus.ON_THE_WAY -> Icons.Outlined.LocalShipping
                    OrderStatus.DELIVERED -> Icons.Outlined.CheckCircle
                    OrderStatus.CANCELLED -> Icons.Outlined.Cancel
                },
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun PriceRow(label: String, value: Double, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(
            "$${"%.2f".format(value)}",
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
    }
}
