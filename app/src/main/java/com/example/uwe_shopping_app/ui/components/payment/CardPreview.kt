package com.example.uwe_shopping_app.ui.components.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardPreview(
    cardNumber: String,
    cardholderName: String,
    expiryMonth: String,
    expiryYear: String,
    cardType: CardType,
    modifier: Modifier = Modifier
) {
    val cardColor = when (cardType) {
        CardType.VISA -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF1E3A8A),
                Color(0xFF3B82F6)
            )
        )
        CardType.MASTERCARD -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFFFB800),
                Color(0xFFFF6B00)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section - Card brand
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = cardType.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Middle section - Card number
            Text(
                text = formatCardNumberPreview(cardNumber),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom section - Cardholder name and expiry
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "CARDHOLDER NAME",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = cardholderName.ifEmpty { "NAME" }.uppercase(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "VALID THRU",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (expiryMonth.isNotEmpty() && expiryYear.isNotEmpty()) {
                            "${expiryMonth.padStart(2, '0')}/${expiryYear.takeLast(2)}"
                        } else {
                            "MM/YY"
                        },
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun formatCardNumberPreview(cardNumber: String): String {
    val cleaned = cardNumber.replace(" ", "").replace("-", "")
    if (cleaned.isEmpty()) {
        return "**** **** **** ****"
    }
    val formatted = cleaned.chunked(4).joinToString(" ")
    val remaining = 16 - cleaned.length
    return if (remaining > 0) {
        formatted + " " + "*".repeat(remaining).chunked(4).joinToString(" ")
    } else {
        formatted.take(19) // Max 4 groups of 4 digits with spaces
    }
}

