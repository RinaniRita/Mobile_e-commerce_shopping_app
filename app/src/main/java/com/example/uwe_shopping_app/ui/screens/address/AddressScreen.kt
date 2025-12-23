package com.example.uwe_shopping_app.ui.screens.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.ui.components.address.AddressCard
import com.example.uwe_shopping_app.ui.components.address.AddressType
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    viewModel: AddressViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onAddNewClick: () -> Unit = {},
    onEditClick: (AddressUiModel) -> Unit = {},
    onAddressSelected: (AddressUiModel) -> Unit = {}
) {
    val addresses by viewModel.addresses.collectAsState()

    AddressScreenContent(
        modifier = modifier,
        addresses = addresses,
        onBackClick = onBackClick,
        onAddNewClick = onAddNewClick,
        onEditClick = { address ->
            viewModel.selectAddress(address.id)
            onEditClick(address)
        },
        onAddressSelected = { address ->
            viewModel.selectAddress(address.id)
            onAddressSelected(address.copy(isSelected = true))
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddressScreenPreview() {
    Uwe_shopping_appTheme {
        AddressScreenContentPreview()
    }
}

@Composable
private fun AddressScreenContentPreview() {
    val sample = listOf(
        AddressUiModel(
            id = 1,
            title = "SEND TO",
            recipient = "My Office",
            addressLine = "SBI Building, street 3, Software Park",
            type = AddressType.OFFICE,
            isSelected = true
        ),
        AddressUiModel(
            id = 2,
            title = "SEND TO",
            recipient = "My Home",
            addressLine = "SBI Building, street 3, Software Park",
            type = AddressType.HOME
        )
    )

    AddressScreenContent(
        addresses = sample,
        onBackClick = {},
        onAddNewClick = {},
        onEditClick = {},
        onAddressSelected = {}
    )
}

@Composable
private fun AddressScreenContent(
    addresses: List<AddressUiModel>,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onAddNewClick: () -> Unit,
    onEditClick: (AddressUiModel) -> Unit,
    onAddressSelected: (AddressUiModel) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Delivery address",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(addresses, key = { it.id }) { address ->
                AddressCard(
                    address = address,
                    onSelect = { onAddressSelected(address) },
                    onEdit = { onEditClick(address) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddNewClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D2D2D))
        ) {
            Text(
                text = "Add new address",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

