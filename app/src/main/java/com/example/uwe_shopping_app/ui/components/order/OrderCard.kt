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

data class OrderItem(
    val orderId: String,
    val trackingNumber: String,
    val quantity: Int,
    val subtotal: Double,
    val date: String,
    val status: OrderStatus
)

@Composable
fun OrderCard(
    order: OrderItem,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top: Order ID + Date
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

            Spacer(modifier = Modifier.height(8.dp))

            // Tracking number
            Text(
                text = "Tracking number: ${order.trackingNumber}",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            // Quantity + Subtotal
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Quantity: ${order.quantity}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Subtotal: $${order.subtotal}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status + Details button
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OrderStatusLabel(status = order.status)

                OutlinedButton(
                    onClick = onDetailsClick,
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Details")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderCard() {
    val sampleOrder = OrderItem(
        orderId = "1524",
        trackingNumber = "IK287368838",
        quantity = 2,
        subtotal = 110.0,
        date = "13/05/2021",
        status = OrderStatus.PENDING
    )

    OrderCard(
        order = sampleOrder,
        onDetailsClick = {}
    )
}
