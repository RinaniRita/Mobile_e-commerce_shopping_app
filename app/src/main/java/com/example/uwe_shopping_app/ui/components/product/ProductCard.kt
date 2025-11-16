package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.domain.model.Product
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE8E8E8),
                                Color(0xFFD0D0D0)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    // If drawable resource ID is provided, use it
                    product.imageResId != null -> {
                        Image(
                            painter = painterResource(id = product.imageResId),
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    // If image URL is provided, show placeholder (you can add Coil later)
                    product.imageUrl != null -> {
                        // Placeholder for URL images - add Coil AsyncImage here if needed
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📷",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    // No image - show placeholder
                    else -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📷",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }

            // Product Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "$${String.format(Locale.getDefault(), "%.2f", product.price)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 180)
@Composable
fun ProductCardPreview() {
    Uwe_shopping_appTheme {
        ProductCard(
            product = Product(
                id = "1",
                name = "Long Sleeve Dress",
                price = 45.00,
                //imageResId = R.drawable.base_admin_chan_pfp
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 180)
@Composable
fun ProductCardPreview2() {
    Uwe_shopping_appTheme {
        ProductCard(
            product = Product(
                id = "2",
                name = "Sportwear Set",
                price = 80.00,
                //imageResId = R.drawable.base_admin_chan_pfp
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 180)
@Composable
fun ProductCardPreview3() {
    Uwe_shopping_appTheme {
        ProductCard(
            product = Product(
                id = "3",
                name = "Sweater",
                price = 35.00,
                //imageResId = R.drawable.base_admin_chan_pfp
            )
        )
    }
}
