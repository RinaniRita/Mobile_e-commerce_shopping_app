package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.domain.model.Product

@Composable
fun ImageCarousel(
    product: Product,
    currentImageIndex: Int,
    onImageIndexChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageResIds = product.imageResIds ?: product.imageResId?.let { listOf(it) } ?: emptyList()
    val imageUrls = product.imageUrls ?: product.imageUrl?.let { listOf(it) } ?: emptyList()
    
    val totalImages = maxOf(imageResIds.size, imageUrls.size)
    
    if (totalImages == 0) {
        // Placeholder when no images
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFF5E6D3),
                            Color(0xFFE8D5C4)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder content
        }
        return
    }
    
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = currentImageIndex)
    
    val currentIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    
    LaunchedEffect(currentIndex) {
        onImageIndexChanged(currentIndex)
    }
    
    LaunchedEffect(currentImageIndex) {
        if (currentImageIndex != listState.firstVisibleItemIndex) {
            listState.animateScrollToItem(currentImageIndex)
        }
    }
    
    Box(
        modifier = modifier
    ) {
        // Background circle
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
        
        // Image carousel using LazyRow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 32.dp, vertical = 50.dp)
        ) {
            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                items(totalImages) { page ->
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            page < imageResIds.size && imageResIds.isNotEmpty() -> {
                                Image(
                                    painter = painterResource(id = imageResIds[page]),
                                    contentDescription = product.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            page < imageUrls.size && imageUrls.isNotEmpty() -> {
                                // Placeholder for URL images - you can add Coil AsyncImage here
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFE0E0E0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Placeholder
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Page indicators
        if (totalImages > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(totalImages) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (index == currentIndex) {
                                    Color.Black
                                } else {
                                    Color(0xFFE0E0E0)
                                }
                            )
                    )
                }
            }
        }
    }
}
