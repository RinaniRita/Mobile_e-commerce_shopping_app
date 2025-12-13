package com.example.uwe_shopping_app.ui.screens.cart

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.uwe_shopping_app.ui.components.cart.CartHeader
import com.example.uwe_shopping_app.ui.components.cart.CartItemCard
import com.example.uwe_shopping_app.ui.components.cart.OrderSummaryCard
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.EmptyState
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun CartScreen(
    navController: NavHostController,
    currentRoute: String,
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val error = uiState.error

    Scaffold(
        topBar = {
            CartHeader(
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute,
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
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCart() }) {
                            Text("Retry")
                        }
                    }
                }
            } else if (uiState.cartItems.isEmpty()) {
                EmptyState(
                    message = "Your cart is empty",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Cart Items List
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
                                onQuantityIncrease = { viewModel.increaseQuantity(it) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Order Summary
                    OrderSummaryCard(
                        productPrice = uiState.productPrice,
                        shipping = "Free shipping",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Proceed to Checkout Button
                    Button(
                        onClick = { navController.navigate("checkout") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF424242) // Dark grey
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

// ================= Screen Previews ===================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    Uwe_shopping_appTheme {
        val viewModel = remember { CartViewModel() }
        val uiState by viewModel.uiState.collectAsState()
        val error = uiState.error

        Scaffold(
            topBar = {
                CartHeader(
                    onBackClick = { }
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = rememberNavController(),
                    currentRoute = "cart",
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
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadCart() }) {
                                Text("Retry")
                            }
                        }
                    }
                } else if (uiState.cartItems.isEmpty()) {
                    EmptyState(
                        message = "Your cart is empty",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Cart Items List
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
                                    onQuantityIncrease = { viewModel.increaseQuantity(it) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Order Summary
                        OrderSummaryCard(
                            productPrice = uiState.productPrice,
                            shipping = "Free shipping",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Proceed to Checkout Button
                        Button(
                            onClick = { viewModel.proceedToCheckout() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF424242) // Dark grey
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
