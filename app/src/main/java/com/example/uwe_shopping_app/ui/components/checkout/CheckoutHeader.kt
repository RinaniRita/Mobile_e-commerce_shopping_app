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
    ) {
        // Top bar with back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
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

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Text(
                text = "Check out",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Progress indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Step 1: Shipping (Location pin)
            StepIndicator(
                icon = Icons.Default.LocationOn,
                isActive = currentStep >= 1,
                isCompleted = currentStep > 1
            )

            // Dotted line
            DottedLine(isActive = currentStep >= 2)

            // Step 2: Payment (Menu icon - horizontal lines)
            StepIndicator(
                icon = Icons.Default.Menu,
                isActive = currentStep >= 2,
                isCompleted = currentStep > 2
            )

            // Dotted line
            DottedLine(isActive = currentStep >= 3)

            // Step 3: Confirmation (Checkmark)
            StepIndicator(
                icon = Icons.Default.Check,
                isActive = currentStep >= 3,
                isCompleted = false
            )
        }
    }
}

@Composable
private fun StepIndicator(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
            .background(
                if (isActive) Color.Black else Color(0xFFE0E0E0),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
            )
    )
}

