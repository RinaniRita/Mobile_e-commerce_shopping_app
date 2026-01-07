package com.example.uwe_shopping_app.ui.components.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.R

@Composable
fun PaymentMethodOption(
    cardType: CardType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageResId = when (cardType) {
        CardType.VISA -> R.drawable.visa
        CardType.MASTERCARD -> R.drawable.mastercard
    }

    Surface(
        modifier = modifier
            .size(70.dp, 50.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = cardType.name,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.6f),
                contentScale = ContentScale.Fit
            )
        }
    }
}

