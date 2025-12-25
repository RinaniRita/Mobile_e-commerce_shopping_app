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

@Composable
fun CartScreen(
    navController: NavHostController,
    currentRoute: String,
    // ViewModel sẽ tự động được khởi tạo đúng cách (bao gồm Application context)
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val error = uiState.error
    var selectedTab by remember { mutableStateOf(CartStatusTab.YOUR_CART) }
    var isSidebarOpen by remember { mutableStateOf(false) }

    // Tự động load lại giỏ hàng mỗi khi vào màn hình này
    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                CartHeader(
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { isSidebarOpen = true }
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
            // Tab trạng thái (Your Cart, Pending...)
            CartStatusTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            // Xử lý các trạng thái UI: Loading, Error, Empty, Content
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
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
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
                // Nội dung chính khi có sản phẩm
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Danh sách sản phẩm
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
                                // GỌI HÀM XÓA TẠI ĐÂY
                                onRemoveClick = { viewModel.removeItem(it) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tổng tiền
                    OrderSummaryCard(
                        productPrice = uiState.productPrice,
                        shipping = "Free shipping",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nút thanh toán
                    Button(
                        onClick = { navController.navigate("checkout") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF424242)
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

        // Sidebar
        Sidebar(
            isOpen = isSidebarOpen,
            onClose = { isSidebarOpen = false },
            navController = navController,
            currentRoute = currentRoute,
            modifier = Modifier.zIndex(10f)
        )
    }
}

// --- Các thành phần phụ trợ (Enum & Tab UI) ---

enum class CartStatusTab(val label: String) {
    YOUR_CART("Your cart"),
    PENDING("Pending"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

@Composable
private fun CartStatusTabs(
    selectedTab: CartStatusTab,
    onTabSelected: (CartStatusTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = CartStatusTab.values()

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(40.dp)
            .border(
                width = 1.dp,
                color = Color(0xFF7B3FF2),
                shape = RoundedCornerShape(50)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected(tab) }
                    .background(
                        color = if (isSelected) Color(0xFF424242) else Color.Transparent,
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    color = if (isSelected) Color.White else Color(0xFF424242),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}