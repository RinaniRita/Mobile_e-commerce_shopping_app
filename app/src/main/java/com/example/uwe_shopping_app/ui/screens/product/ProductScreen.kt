package com.example.uwe_shopping_app.ui.screens.product

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.ui.components.common.CollapsibleHeader
import com.example.uwe_shopping_app.ui.components.product.StarRating
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import java.util.Locale
import com.example.uwe_shopping_app.util.LOGIN_REQUIRED



@Composable
fun ProductScreen(
    productId: Int,
    navController: NavHostController,
    onBack: () -> Unit = {},
    viewModel: ProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current // Lấy context để hiển thị Toast

    // Load products
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    // Add to cart success
    LaunchedEffect(uiState.isAddToCartSuccess) {
        if (uiState.isAddToCartSuccess) {
            Toast.makeText(context, "Added to cart successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    // Error
    LaunchedEffect(uiState.error) {
        when (uiState.error) {
            LOGIN_REQUIRED -> {
                navController.navigate("profile")
                viewModel.clearError()
            }
            null -> Unit
            else -> {
                Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }


    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        bottomBar = {
            AddToCartBar(
                onAddToCart = viewModel::addToCart
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ProductTopBar(
                isFavorite = uiState.isFavorite,
                onFavoriteClick = viewModel::toggleFavorite,
                onBackClick = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                uiState.product?.let { product ->
                    ProductDetailsCard(
                        product = product,
                        avgRating = uiState.avgRating,
                        reviewCount = uiState.reviewCount,
                        showDescription = uiState.showDescription,
                        showReviews = uiState.showReviews,
                        reviews = uiState.reviews,
                        onToggleDescription = viewModel::toggleDescription,
                        onToggleReviews = viewModel::toggleReviews,
                        onAddToCart = viewModel::addToCart
                    )
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = uiState.error ?: "Product not found",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductTopBar(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Icon(
                imageVector = if (isFavorite)
                    Icons.Default.Favorite
                else
                    Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else Color.Black
            )
        }
    }
}

@Composable
private fun ProductDetailsCard(
    product: ProductEntity,
    avgRating: Double,
    reviewCount: Int,
    showDescription: Boolean,
    showReviews: Boolean,
    reviews: List<ProductReviewUi>,
    onToggleDescription: () -> Unit,
    onToggleReviews: () -> Unit,
    onAddToCart: () -> Unit
) {
    val ratingDistribution = remember(reviews) {
        reviews.groupingBy { it.rating }.eachCount()
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Image
            Image(
                painter = painterResource(
                    id = product.imageResId.takeIf { it != 0 }
                        ?: android.R.drawable.ic_menu_gallery
                ),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name + Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format(Locale.getDefault(), "%.2f", product.price)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

//            AVG RATING
            RatingSummaryRow(
                rating = avgRating,
                count = reviewCount,
            )
            Spacer(modifier = Modifier.height(24.dp))

            Divider()

            Spacer(modifier = Modifier.height(24.dp))

            // Description
            CollapsibleHeader(
                title = "Description",
                expanded = showDescription,
                onToggle = onToggleDescription
            )

            if (showDescription) {
                Text(
                    text = product.description,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            CollapsibleHeader(
                title = "Reviews",
                expanded = showReviews,
                onToggle = onToggleReviews
            )

            if (showReviews) {
                RatingBreakdown(
                    avgRating = avgRating,
                    totalReviews = reviewCount,
                    ratingDistribution = ratingDistribution
                )

                Divider()

                ReviewList(reviews)
            }

        }
    }
}

@Composable
fun RatingSummaryRow(
    rating: Double,
    count: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        StarRating(
            rating = rating.toInt(),
            onRatingChange = {},
            enabled = false
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = "($count reviews)",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(Modifier.width(8.dp))

    }
}

@Composable
fun RatingBreakdown(
    avgRating: Double,
    totalReviews: Int,
    ratingDistribution: Map<Int, Int>
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {

        // Top row: big rating + stars
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = String.format("%.1f", avgRating),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "OUT OF 5",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                StarRating(
                    rating = avgRating.toInt(),
                    onRatingChange = {},
                    enabled = false
                )
                Text(
                    text = "$totalReviews ratings",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rating bars (5 → 1)
        (5 downTo 1).forEach { star ->
            val count = ratingDistribution[star] ?: 0
            val progress =
                if (totalReviews == 0) 0f else count.toFloat() / totalReviews

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "$star",
                    modifier = Modifier.width(16.dp),
                    fontSize = 12.sp
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFF4CAF90),
                    modifier = Modifier.size(14.dp)
                )

                Spacer(Modifier.width(8.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF4CAF90),
                    trackColor = Color(0xFFE0E0E0)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(36.dp)
                )
            }
        }
    }

}


@Composable
fun ReviewList(reviews: List<ProductReviewUi>) {
    if (reviews.isEmpty()) {
        Text(
            text = "No reviews yet",
            color = Color.Gray
        )
        return
    }

    Column {
        reviews.forEachIndexed { index, review ->
            Column(modifier = Modifier.padding(vertical = 12.dp)) {

                Text(
                    text = review.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(4.dp))

                StarRating(
                    rating = review.rating,
                    onRatingChange = {},
                    enabled = false
                )

                if (review.comment.isNotBlank()) {
                    Text(
                        text = review.comment,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Divider between each reviews
            if (index < reviews.lastIndex) {
                Divider()
            }
        }
    }
}

@Composable
fun AddToCartBar(
    onAddToCart: () -> Unit
) {
    Surface(
        shadowElevation = 12.dp,
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Add to Cart",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
