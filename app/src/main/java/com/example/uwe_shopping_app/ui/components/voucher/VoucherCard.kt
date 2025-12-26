package com.example.uwe_shopping_app.ui.components.voucher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class VoucherUiModel(
    val id: Int,
    val discount: Int,
    val title: String,
    val description: String,
    val code: String,
    val expirationDay: Int,
    val expirationMonth: String,
    val discountColor: Color
)

@Composable
fun VoucherCard(
    voucher: VoucherUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side - Discount percentage with ticket stub design
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .background(
                        color = voucher.discountColor,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Ticket stub effect - semi-circles on the right edge
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    // Create perforated edge effect with multiple semi-circles
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(15) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
                
                Text(
                    text = "${voucher.discount}%",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Middle section - Voucher details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = voucher.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = voucher.description,
                    fontSize = 12.sp,
                    color = Color(0xFF808080)
                )
                Text(
                    text = "Code: ${voucher.code}",
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }

            // Right section - Expiration date with dashed divider
            Row(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dashed vertical divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(60.dp)
                        .background(Color(0xFFE0E0E0))
                )
                
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Exp.",
                        fontSize = 10.sp,
                        color = Color(0xFF808080)
                    )
                    Text(
                        text = "${voucher.expirationDay}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = voucher.expirationMonth,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

