package com.example.uwe_shopping_app.ui.components.wishlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import java.util.Locale

data class WishlistProduct(
    val id: Int,
    val name: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageResId: Int,
    val rating: Double = 4.5,
    val reviewCount: Int = 38
)

@Composable
fun WishlistProductCard(
    product: WishlistProduct,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Image with heart icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Heart icon with white border
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Remove from wishlist",
                        tint = Color(0xFFFF3B30), // Red color
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Product details
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                // Price
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${
                            String.format(
                                Locale.getDefault(),
                                "%.2f",
                                product.price
                            )
                        }",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    if (product.originalPrice != null) {
                        Text(
                            text = "$${
                                String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    product.originalPrice
                                )
                            }",
                            fontSize = 14.sp,
                            color = Color(0xFF9E9E9E),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StarRating(rating = product.rating)
                    Text(
                        text = "(${product.reviewCount})",
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }
        }
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
        
        // Full stars (green color to match image)
        repeat(fullStars) {
            Text(
                text = "★",
                color = Color(0xFF4CAF50), // Green color for filled stars
                fontSize = 14.sp
            )
        }
        
        // Half star
        if (hasHalfStar) {
            Text(
                text = "★",
                color = Color(0xFF4CAF50),
                fontSize = 14.sp
            )
        }
        
        // Empty stars
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0
        repeat(emptyStars) {
            Text(
                text = "★",
                color = Color(0xFFE0E0E0), // Light gray for empty stars
                fontSize = 14.sp
            )
        }
    }
}

