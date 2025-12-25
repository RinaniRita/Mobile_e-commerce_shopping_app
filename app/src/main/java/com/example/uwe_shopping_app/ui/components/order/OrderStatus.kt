package com.example.uwe_shopping_app.ui.components.order

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp


enum class OrderStatus {
    ON_THE_WAY,
    DELIVERED,
    CANCELLED,
}

@Composable
fun OrderStatusLabel(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (status) {
        OrderStatus.ON_THE_WAY -> "ON THE WAY" to Color(0xFF2196F3)
        OrderStatus.DELIVERED -> "DELIVERED" to Color(0xFF4CAF50)
        OrderStatus.CANCELLED -> "CANCELLED" to Color(0xFFF44336)
    }

    Text(
        text = text,
        color = color,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
    )
}


