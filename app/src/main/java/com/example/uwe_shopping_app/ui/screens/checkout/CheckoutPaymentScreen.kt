package com.example.uwe_shopping_app.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.checkout.CashPaymentOption
import com.example.uwe_shopping_app.ui.components.checkout.CheckoutHeader
import com.example.uwe_shopping_app.ui.screens.payment.PaymentViewModel

@Composable
fun CheckoutPaymentScreen(
    navController: NavHostController,
    productPrice: Double,
    shippingPrice: Double,
    shippingLabel: String,
    address: String,
    phone: String,
    discount: Double,
    viewModel: CheckoutPaymentViewModel = viewModel(),
    paymentViewModel: PaymentViewModel = viewModel()
) {
    var agreeTerms by remember { mutableStateOf(true) }
    val cards by paymentViewModel.cards.collectAsState()
    
    // 0 = Cash, 1, 2, ... = Card ID
    var selectedPaymentMethodId by remember { mutableStateOf<Int>(0) } 

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
                Text(text = "STEP 2", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Payment", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Choose your payment method", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                // CASH OPTION
                CashPaymentOption(
                    selected = selectedPaymentMethodId == 0,
                    onClick = { selectedPaymentMethodId = 0 }
                )

                // CARD OPTIONS
                cards.forEach { card ->
                    CardPaymentOption(
                        last4 = card.cardNumber.takeLast(4),
                        cardType = card.cardType.name,
                        isSelected = selectedPaymentMethodId == card.id,
                        onClick = { selectedPaymentMethodId = card.id }
                    )
                }

                // ADD NEW CARD BUTTON
                OutlinedButton(
                    onClick = { navController.navigate("add_new_card") },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add New Credit Card")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFF7F7F7), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    SummaryRow(label = "Product price", value = "$${"%.2f".format(productPrice)}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE0E0E0))
                    SummaryRow(label = "Shipping ($shippingLabel)", value = "$${"%.2f".format(shippingPrice)}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE0E0E0))
                    SummaryRow(label = "Subtotal", value = "$${"%.2f".format(subtotal)}", isBold = true)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = agreeTerms, onCheckedChange = { agreeTerms = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "I agree to ", fontSize = 14.sp)
                Text(
                    text = "Terms and conditions", 
                    fontSize = 14.sp, 
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate("terms_of_use")
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (agreeTerms) {
                        val finalMethod = if (selectedPaymentMethodId == 0) "Cash" else {
                            val card = cards.find { it.id == selectedPaymentMethodId }
                            "Card (${card?.cardType?.name} ****${card?.cardNumber?.takeLast(4)})"
                        }

                        viewModel.placeOrder(
                            totalPrice = subtotal,
                            address = address,
                            phoneNumber = phone,
                            shippingMethod = finalMethod,
                            shippingFee = shippingPrice,
                            discountAmount = discount
                        ) { orderId ->
                            navController.navigate("checkout_completed") {
                                popUpTo("cart") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                enabled = agreeTerms
            ) {
                Text(text = "Place my order", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CardPaymentOption(last4: String, cardType: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(if (isSelected) Color.Black else Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, if (isSelected) Color.Black else Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.CreditCard,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "$cardType ****$last4",
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pay securely with your credit card",
                    color = if (isSelected) Color.LightGray else Color.Gray,
                    fontSize = 12.sp
                )
            }
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
        Text(text = value, fontSize = 16.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium)
    }
}
