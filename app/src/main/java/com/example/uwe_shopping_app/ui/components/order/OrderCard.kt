package com.example.uwe_shopping_app.ui.components.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

data class OrderItem(
    val orderId: Int,
    val subtotal: Double,
    val date: String,
    val status: OrderStatus,
    val totalItems: Int
)


@Composable
fun OrderCard(
    order: OrderItem,
    onDetailsClick: () -> Unit,
    onConfirmDelivered: () -> Unit,
    onCancelOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onDetailsClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Order ID + Date
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Order #${order.orderId}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = order.date,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtotal
            Text(
                text = "Subtotal: $${order.subtotal}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status + Actions
            OrderStatusLabel(status = order.status)

            if (order.status == OrderStatus.ON_THE_WAY) {

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onConfirmDelivered,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("Confirm Delivered", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = onCancelOrder,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}



