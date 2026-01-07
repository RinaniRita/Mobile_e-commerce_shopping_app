package com.example.uwe_shopping_app.ui.components.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun OrderSummaryCard(
    productPrice: Double,
    shipping: String = "Freeship",
    modifier: Modifier = Modifier
) {
    val subtotal = productPrice

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Product price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Product price",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "$${String.format(Locale.getDefault(), "%.2f", productPrice)}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )

            // Shipping
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Shipping",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = shipping,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )

            // Subtotal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Subtotal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "$${String.format(Locale.getDefault(), "%.2f", subtotal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

