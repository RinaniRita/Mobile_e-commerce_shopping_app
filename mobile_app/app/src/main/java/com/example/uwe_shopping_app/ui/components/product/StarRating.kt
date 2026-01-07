package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun StarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    enabled: Boolean = true
) {
    Row {
        repeat(5) { index ->
            IconButton(
                onClick = { if (enabled) onRatingChange(index + 1) },
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < rating) Color(0xFFFFC107) else Color.LightGray
                )
            }
        }
    }
}

