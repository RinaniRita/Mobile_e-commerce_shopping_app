package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SizeSelector(
    sizes: List<String>,
    selectedIndex: Int,
    onSizeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Size",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            color = Color.Black
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sizes.forEachIndexed { index, size ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (isSelected) Color.Black else Color(0xFFF5F5F5)
                        )
                        .clickable { onSizeSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = size,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = if (isSelected) Color.White else Color(0xFF9E9E9E)
                    )
                }
            }
        }
    }
}
