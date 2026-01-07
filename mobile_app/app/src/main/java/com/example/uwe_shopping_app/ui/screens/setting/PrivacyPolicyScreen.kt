package com.example.uwe_shopping_app.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun PrivacyPolicyScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF222222)
                    )
                },
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
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Privacy Policy",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Last updated: January 1, 2024",
                fontSize = 14.sp,
                color = Color(0xFF808080)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "1. Information We Collect",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "We collect information that you provide directly to us, such as when you create an account, make a purchase, or contact us for support.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "2. How We Use Your Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "We use the information we collect to provide, maintain, and improve our services, process transactions, and communicate with you.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "3. Information Sharing",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "We do not sell, trade, or otherwise transfer your personal information to third parties without your consent, except as described in this policy.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "4. Data Security",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "We implement appropriate security measures to protect your personal information against unauthorized access, alteration, disclosure, or destruction.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

