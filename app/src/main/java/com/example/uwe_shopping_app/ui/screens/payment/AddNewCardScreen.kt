package com.example.uwe_shopping_app.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uwe_shopping_app.ui.components.payment.CardPreview
import com.example.uwe_shopping_app.ui.components.payment.CardType
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AddNewCardScreen(
    navController: NavController,
    viewModel: PaymentViewModel = viewModel(),
    initialCardType: CardType? = null,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var cardholderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryMonth by remember { mutableStateOf("") }
    var expiryYear by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedCardType by remember { mutableStateOf(initialCardType ?: CardType.VISA) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            Text(
                text = "Add new card",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Card Preview
            CardPreview(
                cardNumber = cardNumber,
                cardholderName = cardholderName.ifBlank { "YOUR NAME" },
                expiryMonth = expiryMonth.ifBlank { "MM" },
                expiryYear = expiryYear.ifBlank { "YY" },
                cardType = selectedCardType
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Card Type Selection
            Text(
                text = "Card Type",
                fontSize = 13.sp,
                color = Color(0xFF9E9E9E),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CardTypeOption(
                    cardType = CardType.VISA,
                    isSelected = selectedCardType == CardType.VISA,
                    onClick = { selectedCardType = CardType.VISA },
                    modifier = Modifier.weight(1f)
                )
                CardTypeOption(
                    cardType = CardType.MASTERCARD,
                    isSelected = selectedCardType == CardType.MASTERCARD,
                    onClick = { selectedCardType = CardType.MASTERCARD },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cardholder Name
            PaymentTextField(
                label = "Cardholder Name",
                value = cardholderName,
                onValueChange = { cardholderName = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card Number
            PaymentTextField(
                label = "Card Number",
                value = cardNumber,
                onValueChange = { newValue ->
                    val digitsOnly = newValue.filter { it.isDigit() }
                    if (digitsOnly.length <= 16) {
                        cardNumber = digitsOnly
                    }
                },
                visualTransformation = CardNumberTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Expires and CVV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PaymentTextField(
                    label = "Expires",
                    value = expiryMonth + expiryYear,
                    onValueChange = { newValue ->
                        val cleaned = newValue.filter { it.isDigit() }
                        if (cleaned.length <= 4) {
                            if (cleaned.length <= 2) {
                                expiryMonth = cleaned
                                expiryYear = ""
                            } else {
                                expiryMonth = cleaned.take(2)
                                expiryYear = cleaned.drop(2)
                            }
                        }
                    },
                    placeholder = "MM/YY",
                    visualTransformation = ExpiryDateTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                PaymentTextField(
                    label = "CVV",
                    value = cvv,
                    onValueChange = { newValue ->
                        val digitsOnly = newValue.filter { it.isDigit() }
                        if (digitsOnly.length <= 3) {
                            cvv = digitsOnly
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Add Card Button
            Button(
                onClick = {
                    viewModel.addCard(
                        cardholderName = cardholderName,
                        cardNumber = cardNumber,
                        expiryMonth = expiryMonth,
                        expiryYear = expiryYear,
                        cardType = selectedCardType,
                        onSuccess = { navController.popBackStack() },
                        onError = { errorMessage = it }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF424242)
                )
            ) {
                Text(
                    text = "Add card",
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
private fun PaymentTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF9E9E9E),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color(0xFF9E9E9E)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE0E0E0),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5)
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun CardTypeOption(
    cardType: CardType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(50.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFFE3F2FD) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF2196F3) else Color(0xFFE0E0E0)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = cardType.name,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF2196F3) else Color.Black
            )
        }
    }
}

/**
 * VisualTransformation for MM/YY format
 */
class ExpiryDateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

/**
 * VisualTransformation for Card Number format (adds space every 4 digits)
 */
class CardNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 15) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
