package com.example.uwe_shopping_app.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.cart.CartHeader
import com.example.uwe_shopping_app.ui.components.cart.CartItemCard
import com.example.uwe_shopping_app.ui.components.cart.OrderSummaryCard
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.EmptyState
import com.example.uwe_shopping_app.ui.components.common.Sidebar
import com.example.uwe_shopping_app.ui.components.common.EmptyState
import com.example.uwe_shopping_app.ui.components.common.ShopStatusTabs
import com.example.uwe_shopping_app.ui.components.common.ShopTab


@Composable
fun CartScreen(
    navController: NavHostController,
    currentRoute: String,
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    var selectedTab by remember { mutableStateOf(ShopTab.YOUR_CART) }

    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    Scaffold(
        topBar = {
            CartHeader(
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {

            // ---------------- STATUS TABS ----------------
            ShopStatusTabs(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    if (tab != ShopTab.YOUR_CART) {
                        navController.navigate("orders?status=${tab.name}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            // ================= NOT LOGGED IN =================
            if (!isLoggedIn) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Please log in to use your cart",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Text(
                            text = "Log in to add items and checkout",
                            color = Color.Gray
                        )

                        Button(
                            onClick = { navController.navigate("login") },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Log in / Sign up",
                                color = Color.White
                            )
                        }
                    }
                }
                return@Scaffold
            }

            // ================= LOGGED IN =================
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.error ?: "Unknown error",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.loadCart() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black
                                )
                            ) {
                                Text(
                                    text = "Retry",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                uiState.cartItems.isEmpty() -> {
                    EmptyState(
                        message = "Your cart is empty",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            uiState.cartItems.forEach { item ->
                                CartItemCard(
                                    item = item,
                                    onToggleSelection = { viewModel.toggleItemSelection(it) },
                                    onQuantityDecrease = { viewModel.decreaseQuantity(it) },
                                    onQuantityIncrease = { viewModel.increaseQuantity(it) },
                                    onRemoveClick = { viewModel.removeItem(it) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OrderSummaryCard(
                            productPrice = uiState.productPrice,
                            shipping = "Free shipping",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                navController.navigate(
                                    "address?from=checkout&totalPrice=${uiState.productPrice}"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Proceed to checkout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
