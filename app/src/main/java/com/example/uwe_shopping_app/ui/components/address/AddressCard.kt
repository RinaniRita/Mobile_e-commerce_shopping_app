package com.example.uwe_shopping_app.ui.components.address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

data class AddressUiModel(
    val id: Int,
    val title: String,
    val recipient: String,
    val addressLine: String,
    val type: AddressType,
    val isSelected: Boolean = false
)

enum class AddressType { HOME, OFFICE }

@Composable
fun AddressCard(
    address: AddressUiModel,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = address.isSelected,
                        onClick = onSelect
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    AddressIcon(type = address.type)
                }

                Text(
                    text = "Edit",
                    color = Color(0xFFEB5757),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(onClick = onEdit)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = address.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(
                text = address.recipient,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = address.addressLine,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AddressIcon(type: AddressType) {
    val icon = when (type) {
        AddressType.HOME -> Icons.Outlined.Home
        AddressType.OFFICE -> Icons.Outlined.Apartment
    }

    Box(
        modifier = Modifier
            .padding(start = 4.dp)
            .size(36.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressCardPreview() {
    Uwe_shopping_appTheme {
        AddressCard(
            address = AddressUiModel(
                id = 1,
                title = "SEND TO",
                recipient = "My Office",
                addressLine = "SBI Building, street 3, Software Park",
                type = AddressType.OFFICE,
                isSelected = true
            ),
            onSelect = {},
            onEdit = {}
        )
    }
}

