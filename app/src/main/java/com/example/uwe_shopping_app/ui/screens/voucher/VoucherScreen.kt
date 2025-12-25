package com.example.uwe_shopping_app.ui.screens.voucher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.ui.components.voucher.VoucherCard
import com.example.uwe_shopping_app.ui.components.voucher.VoucherUiModel
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun VoucherScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Sample voucher data - matching the image
    val vouchers = listOf(
        VoucherUiModel(
            id = 1,
            discount = 50,
            title = "Black Friday",
            description = "Sale off 50%",
            code = "fridaysale",
            expirationDay = 20,
            expirationMonth = "Dec",
            discountColor = Color(0xFF424242) // Dark gray
        ),
        VoucherUiModel(
            id = 2,
            discount = 30,
            title = "Holiday Sale",
            description = "Sale off 30%",
            code = "holiday30",
            expirationDay = 22,
            expirationMonth = "Dec",
            discountColor = Color(0xFF757575) // Medium gray
        ),
        VoucherUiModel(
            id = 3,
            discount = 20,
            title = "First order",
            description = "20% off your first order",
            code = "welcome",
            expirationDay = 28,
            expirationMonth = "Dec",
            discountColor = Color(0xFF9E9E9E) // Light gray
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Back button with circular background
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF5F5F5), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Title
            Text(
                text = "Voucher",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                fontSize = 20.sp
            )
        }

        // Voucher list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(vouchers, key = { it.id }) { voucher ->
                VoucherCard(voucher = voucher)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VoucherScreenPreview() {
    Uwe_shopping_appTheme {
        VoucherScreen()
    }
}

