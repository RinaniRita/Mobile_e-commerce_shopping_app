package com.example.uwe_shopping_app.ui.components.voucher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.data.local.entity.VoucherEntity

@Composable
fun VoucherCard(
    voucher: VoucherEntity,
    onCopyClick: (String) -> Unit,
    onSelectClick: (VoucherEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val discountColor = when (voucher.discountValue) {
        50 -> Color(0xFF424242)
        30 -> Color(0xFF757575)
        else -> Color(0xFF9E9E9E)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { onSelectClick(voucher) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .background(
                        color = discountColor,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(15) {
                        Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
                    }
                }
                
                Text(
                    text = if (voucher.discountType == "PERCENTAGE") "${voucher.discountValue}%" else "$${voucher.discountValue}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = voucher.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = voucher.description, fontSize = 12.sp, color = Color.Gray)
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Code: ${voucher.code}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    IconButton(onClick = { onCopyClick(voucher.code) }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                    }
                }
                
                // Show usage limit
                Text(
                    text = "Used: ${voucher.usageCount}/${voucher.maxUsage} times",
                    fontSize = 10.sp,
                    color = if (voucher.usageCount >= voucher.maxUsage) Color.Red else Color.Gray
                )
            }

            Row(
                modifier = Modifier.width(70.dp).fillMaxHeight().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.width(1.dp).height(60.dp).background(Color(0xFFE0E0E0)))
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = "Exp.", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "${voucher.expiryDay}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = voucher.expiryMonth, fontSize = 12.sp)
                }
            }
        }
    }
}
