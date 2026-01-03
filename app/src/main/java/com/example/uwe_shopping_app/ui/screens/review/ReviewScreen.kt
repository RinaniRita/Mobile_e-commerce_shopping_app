package com.example.uwe_shopping_app.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.ui.components.product.StarRating

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    orderId: Int,
    userId: Int,
    onBack: () -> Unit,
    viewModel: ReviewViewModel = viewModel()
) {
    val items = viewModel.items

    LaunchedEffect(orderId) {
        viewModel.load(orderId, userId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Rate Products") },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            items.forEach { item ->

                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(8.dp))

                StarRating(
                    rating = item.rating,
                    onRatingChange = { viewModel.setRating(item.productId, it) }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = item.comment,
                    onValueChange = {
                        viewModel.setComment(item.productId, it)
                    },
                    enabled = !item.alreadyReviewed,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Write a review...") }
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    enabled = !item.alreadyReviewed && item.rating > 0,
                    onClick = {
                        viewModel.submit(
                            productId = item.productId,
                            userId = userId,
                            orderId = orderId
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(
                        if (item.alreadyReviewed) "Reviewed"
                        else "Submit Review",
                        color = Color.White
                    )
                }

                Divider(Modifier.padding(vertical = 16.dp))
            }
        }
    }
}

