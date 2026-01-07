package com.example.uwe_shopping_app.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.checkout.CheckoutHeader
import com.example.uwe_shopping_app.ui.components.checkout.ShippingMethodRadio

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    viewModel: CheckoutViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            CheckoutHeader(
                onBackClick = { navController.popBackStack() },
                currentStep = 1
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
                .padding(16.dp)
        ) {
            Text(
                text = "Shipping",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Delivery Address", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${uiState.firstName} ${uiState.lastName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${uiState.streetName}, ${uiState.district}, ${uiState.city}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = uiState.phoneNumber,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "Change",
                        color = Color(0xFF7B3FF2),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { 
                            navController.navigate("address?from=checkout&totalPrice=${uiState.productPrice}") 
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.verifyAddressAndCalculateFee() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B3FF2),
                    disabledContainerColor = Color.LightGray
                ),
                enabled = !uiState.isCalculating,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (uiState.isCalculating) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Calculating fee...", fontSize = 14.sp)
                } else {
                    Text("Verify Address & Calculate Fee", fontWeight = FontWeight.SemiBold)
                }
            }

            if (uiState.calculationError != null) {
                Text(
                    text = uiState.calculationError!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (uiState.distanceKm != null) {
                Text(
                    text = "Distance to Shop: ${"%.2f".format(uiState.distanceKm)} km",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Shipping method", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            viewModel.shippingMethods.forEach { method ->
                ShippingMethodRadio(
                    method = method,
                    isSelected = uiState.selectedShippingMethod == method.id,
                    onSelect = { viewModel.selectShippingMethod(method.id) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Coupon Code", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = uiState.couponCode,
                    onValueChange = viewModel::updateCouponCode,
                    placeholder = { Text("Have a code? type it here...", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
                Button(
                    onClick = { viewModel.validateCouponCode() },
                    modifier = Modifier.height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Validate")
                }
            }
            
            if (uiState.voucherError != null) {
                Text(text = uiState.voucherError!!, color = Color.Red, fontSize = 12.sp)
            } else if (uiState.appliedVoucher != null) {
                Text(text = "Applied: ${uiState.appliedVoucher!!.title}", color = Color(0xFF4CAF50), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Billing Address", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.copyBillingAddress,
                    onCheckedChange = { viewModel.toggleCopyBillingAddress() }
                )
                Text(text = "Copy address data from shipping")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SummaryLine("Product Price", "$${uiState.productPrice}")
                if (uiState.itemDiscount > 0) SummaryLine("Voucher Discount", "-$${uiState.itemDiscount}", Color.Red)
                SummaryLine("Shipping fee", "$${uiState.methodPrice + uiState.distancePrice}")
                if (uiState.shippingDiscount > 0) SummaryLine("Shipping Discount", "-$${uiState.shippingDiscount}", Color.Red)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                SummaryLine("Total", "$${uiState.grandTotal}", Color.Black, isBold = true)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { 
                    val fullAddress = "${uiState.firstName} ${uiState.lastName}, ${uiState.streetName}, ${uiState.district}, ${uiState.city}"
                    val totalDiscount = uiState.itemDiscount + uiState.shippingDiscount
                    
                    navController.navigate(
                        "checkout_payment?productPrice=${uiState.finalProductPrice}" +
                        "&shippingPrice=${uiState.finalShippingPrice}" +
                        "&shippingLabel=${uiState.shippingLabel}" +
                        "&address=$fullAddress" +
                        "&phone=${uiState.phoneNumber}" +
                        "&discount=$totalDiscount"
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = uiState.distanceKm != null,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Continue to payment", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SummaryLine(label: String, value: String, color: Color = Color.Gray, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = color, fontSize = 16.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium)
    }
}
