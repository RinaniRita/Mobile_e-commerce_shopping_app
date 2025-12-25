package com.example.uwe_shopping_app.ui.screens.checkout

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.checkout.CashPaymentOption
import com.example.uwe_shopping_app.ui.components.checkout.CheckoutHeader

@Composable
fun CheckoutPaymentScreen(
    navController: NavHostController,
    productPrice: Double,
    shippingPrice: Double,
    shippingLabel: String
) {
    var agreeTerms by remember { mutableStateOf(true) }

    // TÍNH TOÁN TỔNG TIỀN THỰC TẾ
    val subtotal = productPrice + shippingPrice

    Scaffold(
        topBar = {
            CheckoutHeader(
                onBackClick = { navController.popBackStack() },
                currentStep = 2
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "STEP 2",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Payment",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Choose your payment method",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                CashPaymentOption(
                    selected = true,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // HIỂN THỊ CHI TIẾT GIÁ TIỀN THỰC TẾ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFF7F7F7), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SummaryRow(label = "Product price", value = "$$productPrice")
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE0E0E0)
                    )
                    // HIỂN THỊ PHÍ SHIP VÀ TÊN PHƯƠNG THỨC GIAO HÀNG
                    SummaryRow(label = "Shipping ($shippingLabel)", value = "$$shippingPrice")
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE0E0E0)
                    )
                    SummaryRow(label = "Subtotal", value = "$$subtotal", isBold = true)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeTerms,
                    onCheckedChange = { agreeTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "I agree to ", fontSize = 14.sp, color = Color.Black)
                Text(
                    text = "Terms and conditions",
                    fontSize = 14.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (agreeTerms) {
                        navController.navigate("checkout_completed") {
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                enabled = agreeTerms
            ) {
                Text(
                    text = "Place my order",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = Color.Black
        )
    }
}
