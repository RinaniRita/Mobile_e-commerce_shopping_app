package com.example.uwe_shopping_app.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.uwe_shopping_app.ui.components.checkout.CheckoutFormField
import com.example.uwe_shopping_app.ui.components.checkout.CheckoutHeader
import com.example.uwe_shopping_app.ui.components.checkout.ShippingMethodRadio
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

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
        ) {
            // Step indicator text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "STEP 1",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Shipping",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Form fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // First name and Last name row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CheckoutFormField(
                        label = "First name",
                        value = uiState.firstName,
                        onValueChange = viewModel::updateFirstName,
                        modifier = Modifier.weight(1f)
                    )
                    CheckoutFormField(
                        label = "Last name",
                        value = uiState.lastName,
                        onValueChange = viewModel::updateLastName,
                        error = uiState.lastNameError,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Country
                CheckoutFormField(
                    label = "Country",
                    value = uiState.country,
                    onValueChange = viewModel::updateCountry,
                    error = uiState.countryError,
                    isDropdown = true,
                    onDropdownClick = { /* Handle dropdown */ }
                )

                // Street name
                CheckoutFormField(
                    label = "Street name",
                    value = uiState.streetName,
                    onValueChange = viewModel::updateStreetName,
                    error = uiState.streetNameError
                )

                // City
                CheckoutFormField(
                    label = "City",
                    value = uiState.city,
                    onValueChange = viewModel::updateCity,
                    error = uiState.cityError
                )

                // State / Province
                CheckoutFormField(
                    label = "State / Province",
                    value = uiState.stateProvince,
                    onValueChange = viewModel::updateStateProvince,
                    isRequired = false
                )

                // Zip-code
                CheckoutFormField(
                    label = "Zip-code",
                    value = uiState.zipCode,
                    onValueChange = viewModel::updateZipCode,
                    error = uiState.zipCodeError,
                    keyboardType = KeyboardType.Number
                )

                // Phone number
                CheckoutFormField(
                    label = "Phone number",
                    value = uiState.phoneNumber,
                    onValueChange = viewModel::updatePhoneNumber,
                    error = uiState.phoneNumberError,
                    keyboardType = KeyboardType.Phone
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Shipping method section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Shipping method",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                viewModel.shippingMethods.forEach { method ->
                    ShippingMethodRadio(
                        method = method,
                        isSelected = uiState.selectedShippingMethod == method.id,
                        onSelect = { viewModel.selectShippingMethod(method.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Coupon Code section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Coupon Code",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.couponCode,
                        onValueChange = viewModel::updateCouponCode,
                        placeholder = {
                            Text(
                                text = "Have a code? type it here...",
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFFE0E0E0),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = MaterialTheme.shapes.small
                    )

                    Button(
                        onClick = { viewModel.validateCouponCode() },
                        modifier = Modifier.height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF424242)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Validate",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Billing Address section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Billing Address",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.copyBillingAddress,
                        onCheckedChange = { viewModel.toggleCopyBillingAddress() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF4CAF50)
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Copy address data from shipping",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Continue to payment button
            Button(
                onClick = {
                    if (viewModel.validateForm()) {
                        // TODO: Navigate to payment screen when implemented
                        // navController.navigate("checkout_payment")
                    }
                },
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
                    text = "Continue to payment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ================= Screen Previews ===================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckoutScreenPreview() {
    Uwe_shopping_appTheme {
        CheckoutScreen(
            navController = rememberNavController()
        )
    }
}

