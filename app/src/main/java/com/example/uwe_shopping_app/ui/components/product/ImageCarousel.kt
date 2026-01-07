package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.data.local.entity.ProductEntity

@Composable
fun ImageCarousel(
    product: ProductEntity,
    modifier: Modifier = Modifier
) {
    val imageResIds = listOf(product.imageResId)

    val listState = rememberLazyListState()

    Box(modifier = modifier) {

        // Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFF5E6D3),
                            Color(0xFFE8D5C4)
                        ),
                        radius = 800f
                    )
                )
        )

        // Image
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 32.dp, vertical = 50.dp)
        ) {
            items(imageResIds.size) {
                Image(
                    painter = painterResource(id = imageResIds[it]),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Indicator (single dot for now)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            )
        }
    }
}
