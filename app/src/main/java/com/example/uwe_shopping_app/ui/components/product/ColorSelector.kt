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
fun ColorSelector(
    colors: List<Long>,
    selectedIndex: Int,
    onColorSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (colors.isEmpty()) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Color",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            color = Color.Black
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            colors.forEachIndexed { index, colorValue ->
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color.Black else Color(0xFFE0E0E0),
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 24.dp else 28.dp)
                            .clip(CircleShape)
                            .background(Color(colorValue))
                    )
                }
            }
        }
    }
}
