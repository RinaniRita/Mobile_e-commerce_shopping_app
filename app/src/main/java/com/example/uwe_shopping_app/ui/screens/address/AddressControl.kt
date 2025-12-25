@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uwe_shopping_app.ui.screens.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.address.AddressType
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import com.example.uwe_shopping_app.ui.components.checkout.CheckoutFormField

@Composable
fun AddressControl(
    onBackClick: () -> Unit,
    onSaveClick: (AddressUiModel) -> Unit,
    onDeleteClick: (AddressUiModel) -> Unit = {},
    addressToEdit: AddressUiModel? = null
) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val defaultPhone by session.userPhone.collectAsState(initial = "")

    var firstName by remember { mutableStateOf(addressToEdit?.recipient?.split(" ")?.firstOrNull() ?: "") }
    var lastName by remember { mutableStateOf(if ((addressToEdit?.recipient?.split(" ")?.size ?: 0) > 1) addressToEdit?.recipient?.split(" ")?.drop(1)?.joinToString(" ") ?: "" else "") }
    var streetName by remember { mutableStateOf(addressToEdit?.addressLine ?: "") }
    var city by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    
    // Tự động lấy số điện thoại gốc nếu là thêm mới
    var phoneNumber by remember { mutableStateOf(addressToEdit?.phoneNumber ?: "") }
    
    LaunchedEffect(defaultPhone) {
        if (addressToEdit == null && phoneNumber.isEmpty()) {
            phoneNumber = defaultPhone ?: ""
        }
    }

    var addressType by remember { mutableStateOf(addressToEdit?.type ?: AddressType.HOME) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (addressToEdit == null) "Add New Address" else "Edit Address",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (addressToEdit != null) {
                        IconButton(onClick = { onDeleteClick(addressToEdit) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CheckoutFormField(
                    label = "First name",
                    value = firstName,
                    onValueChange = { firstName = it },
                    modifier = Modifier.weight(1f)
                )
                CheckoutFormField(
                    label = "Last name",
                    value = lastName,
                    onValueChange = { lastName = it },
                    modifier = Modifier.weight(1f)
                )
            }

            CheckoutFormField(
                label = "Street name",
                value = streetName,
                onValueChange = { streetName = it }
            )

            CheckoutFormField(
                label = "City",
                value = city,
                onValueChange = { city = it }
            )

            CheckoutFormField(
                label = "Zip-code",
                value = zipCode,
                onValueChange = { zipCode = it },
                keyboardType = KeyboardType.Number
            )

            CheckoutFormField(
                label = "Phone number",
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                keyboardType = KeyboardType.Phone
            )

            Text("Address Type", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = addressType == AddressType.HOME,
                    onClick = { addressType = AddressType.HOME }
                )
                Text("Home")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = addressType == AddressType.OFFICE,
                    onClick = { addressType = AddressType.OFFICE }
                )
                Text("Office")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val newAddress = AddressUiModel(
                        id = addressToEdit?.id ?: 0,
                        title = if (addressType == AddressType.HOME) "HOME" else "OFFICE",
                        recipient = "$firstName $lastName",
                        addressLine = streetName,
                        phoneNumber = phoneNumber,
                        type = addressType,
                        isSelected = addressToEdit?.isSelected ?: false
                    )
                    onSaveClick(newAddress)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242))
            ) {
                Text("Save Address", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
