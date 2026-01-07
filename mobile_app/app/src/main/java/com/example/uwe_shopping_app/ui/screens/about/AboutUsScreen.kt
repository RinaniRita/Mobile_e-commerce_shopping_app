package com.example.uwe_shopping_app.ui.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
fun AboutUsScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "About us",
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            
            // App Icon/Logo placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "App Icon",
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF808080)
                )
            }
            
            Spacer(Modifier.height(32.dp))
            
            // App Name
            Text(
                text = "UWE Shopping App",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )
            
            Spacer(Modifier.height(8.dp))
            
            // Version
            Text(
                text = "Version 1.0.0",
                fontSize = 14.sp,
                color = Color(0xFF808080)
            )
            
            Spacer(Modifier.height(48.dp))
            
            // About Text
            Text(
                text = "Welcome to UWE Shopping App!",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            )
            
            Spacer(Modifier.height(16.dp))
            
            Text(
                text = "We are committed to providing you with the best shopping experience. " +
                        "Our app offers a wide range of products from various categories including " +
                        "fashion, electronics, beauty, home, and sports.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )
            
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Our team consists of Tran Duy Anh, Nguyen Duc Trung, Nguyen Nhat Minh",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "Thank you for choosing UWE Shopping App!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF222222)
            )
            
            Spacer(Modifier.height(40.dp))
        }
    }
}

