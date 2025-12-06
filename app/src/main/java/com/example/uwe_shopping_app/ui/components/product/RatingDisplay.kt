package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RatingDisplay(
    rating: Double,
    reviewCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Star rating display
        StarRating(rating = rating)
        
        // Review count
        Text(
            text = "($reviewCount)",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF9E9E9E),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val fullStars = rating.toInt()
        val hasHalfStar = (rating - fullStars) >= 0.5
        
        // Full stars
        repeat(fullStars) {
            Text(
                text = "★",
                color = Color(0xFF2D9CDB), // Teal color for filled stars
                fontSize = 16.sp
            )
        }
        
        // Half star
        if (hasHalfStar) {
            Text(
                text = "★",
                color = Color(0xFF2D9CDB),
                fontSize = 16.sp
            )
        }
        
        // Empty stars
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0
        repeat(emptyStars) {
            Text(
                text = "★",
                color = Color(0xFFE0E0E0), // Light gray for empty stars
                fontSize = 16.sp
            )
        }
    }
}
