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
fun TermsOfUseScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Terms of Use",
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
                text = "Terms of Use",
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
                text = "1. Acceptance of Terms",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "By accessing and using the UWE Shopping App, you accept and agree to be bound by the terms and provision of this agreement.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "2. Use License",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Permission is granted to temporarily download one copy of the materials on UWE Shopping App for personal, non-commercial transitory viewing only.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "3. Disclaimer",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "The materials on UWE Shopping App are provided on an 'as is' basis. UWE Shopping App makes no warranties, expressed or implied, and hereby disclaims and negates all other warranties including without limitation, implied warranties or conditions of merchantability, fitness for a particular purpose, or non-infringement of intellectual property or other violation of rights.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

