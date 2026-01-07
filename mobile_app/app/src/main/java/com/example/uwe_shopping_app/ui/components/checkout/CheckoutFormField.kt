package com.example.uwe_shopping_app.ui.components.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckoutFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = true,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isDropdown: Boolean = false,
    onDropdownClick: (() -> Unit)? = null,
    enabled: Boolean = true // THÊM THAM SỐ NÀY
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Label
        Text(
            text = if (isRequired) "$label *" else label,
            fontSize = 14.sp,
            color = if (enabled) Color.Black else Color.Gray,
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Text field
        if (isDropdown) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = true,
                enabled = enabled,
                trailingIcon = {
                    IconButton(onClick = { if (enabled) onDropdownClick?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFF5F5F5), // Màu khi bị disable
                    focusedBorderColor = if (error != null) Color.Red else Color(0xFFE0E0E0),
                    unfocusedBorderColor = if (error != null) Color.Red else Color(0xFFE0E0E0),
                    cursorColor = Color.Black
                ),
                shape = MaterialTheme.shapes.small
            )
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                enabled = enabled, // SỬ DỤNG Ở ĐÂY
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFF5F5F5), // Màu khi bị disable
                    focusedBorderColor = if (error != null) Color.Red else Color(0xFFE0E0E0),
                    unfocusedBorderColor = if (error != null) Color.Red else Color(0xFFE0E0E0),
                    cursorColor = Color.Black
                ),
                shape = MaterialTheme.shapes.small
            )
        }
        
        // Error message
        if (error != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = error,
                fontSize = 12.sp,
                color = Color.Red
            )
        }
    }
}
