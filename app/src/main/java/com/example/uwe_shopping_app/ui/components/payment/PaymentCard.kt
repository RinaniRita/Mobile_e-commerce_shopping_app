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

enum class CardType {
    VISA,
    MASTERCARD
}

data class PaymentCardData(
    val id: Int,
    val cardNumber: String,
    val cardholderName: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cardType: CardType
)

@Composable
fun PaymentCard(
    card: PaymentCardData,
    modifier: Modifier = Modifier
) {
    val cardColor = when (card.cardType) {
        CardType.VISA -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF1E3A8A), // Dark blue
                Color(0xFF3B82F6)  // Light blue
            )
        )
        CardType.MASTERCARD -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFFFB800), // Yellow
                Color(0xFFFF6B00)   // Orange
            )
        )
    }

    Box(
        modifier = modifier
            .width(320.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section - Card brand
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = card.cardType.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Middle section - Card number
            Text(
                text = formatCardNumber(card.cardNumber),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom section - Cardholder name and expiry
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "CARDHOLDER NAME",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.cardholderName.uppercase(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "VALID THRU",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${card.expiryMonth}/${card.expiryYear}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun formatCardNumber(cardNumber: String): String {
    val cleaned = cardNumber.replace(" ", "")
    return cleaned.chunked(4).joinToString(" ")
}

