package com.example.uwe_shopping_app.ui.components.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutHeader(
    onBackClick: () -> Unit,
    currentStep: Int = 1,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .statusBarsPadding()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Check out",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Progress indicator (giữ nguyên logic cũ nhưng làm gọn)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepIndicator(icon = Icons.Default.LocationOn, isActive = currentStep >= 1)
            DottedLine(isActive = currentStep >= 2)
            StepIndicator(icon = Icons.Default.Menu, isActive = currentStep >= 2)
            DottedLine(isActive = currentStep >= 3)
            StepIndicator(icon = Icons.Default.Check, isActive = currentStep >= 3)
        }
    }
}

@Composable
private fun StepIndicator(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(if (isActive) Color.Black else Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) Color.White else Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun DottedLine(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(2.dp)
            .background(if (isActive) Color.Black else Color(0xFFE0E0E0))
    )
}
