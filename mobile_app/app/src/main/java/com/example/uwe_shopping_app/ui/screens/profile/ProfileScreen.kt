package com.example.uwe_shopping_app.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.example.uwe_shopping_app.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {}
) {
    val viewModel: ProfileViewModel = viewModel()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val user by viewModel.user.collectAsState()
    val defaultCard by viewModel.defaultCard.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute,
            )
        }
    ) { innerPadding ->

        // ================= GUEST =================
        if (!isLoggedIn) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(96.dp),
                    tint = Color.Gray
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "You're not logged in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Log in to access your profile, orders and address",
                    color = Color.Gray
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log in / Sign up")
                }
            }

            return@Scaffold
        }

        // ================= LOGGED IN =================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ---------- HEADER ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_placeholder),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = user?.name.orEmpty(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222)
                    )
                    Text(
                        text = user?.email.orEmpty(),
                        fontSize = 14.sp,
                        color = Color(0xFF808080)
                    )
                }

                Spacer(Modifier.weight(1f))

                IconButton(onClick = { onNavigate("profile_setting") }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Edit Profile",
                        tint = Color(0xFF222222)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ---------- MENU ----------
            val menuItems = listOf(
                Triple("Address", Icons.Default.LocationOn, ""),
                Triple("Payment method", Icons.Default.Payment, defaultCard?.let { "**** **** **** ${it.cardNumber.takeLast(4)}" } ?: "Add a card"),
                Triple("Voucher", Icons.Default.LocalOffer, ""),
                Triple("My Wishlist", Icons.Outlined.FavoriteBorder, ""),
                Triple("Log out", Icons.AutoMirrored.Filled.ExitToApp, "")
            )

            menuItems.forEach { (text, icon, subtitle) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (text) {
                                "Address" -> onNavigate("address")
                                "Payment method" -> onNavigate("payment")
                                "Voucher" -> onNavigate("voucher")
                                "My Wishlist" -> onNavigate("wishlist")
                                "Log out" -> {
                                    viewModel.logout()
                                    navController.navigate("home") {
                                        popUpTo(0)
                                    }
                                }
                            }
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF808080),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            color = Color(0xFF808080)
                        )
                        if (subtitle.isNotEmpty()) {
                            Text(
                                text = subtitle,
                                fontSize = 12.sp,
                                color = Color(0xFFB0B0B0)
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF222222)
                    )
                }
            }
        }
    }
}
