package com.example.uwe_shopping_app.ui.screens.address

import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.ui.components.address.AddressType
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Frontend-only ViewModel for the delivery address screen.
 * Holds an in-memory list of addresses and exposes selection logic.
 */
class AddressViewModel : ViewModel() {

    private val _addresses = MutableStateFlow(
        listOf(
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
                type = AddressType.HOME,
                isSelected = false
            )
        )
    )

    val addresses: StateFlow<List<AddressUiModel>> = _addresses.asStateFlow()

    fun selectAddress(id: Int) {
        _addresses.update { list ->
            list.map { address ->
                address.copy(isSelected = address.id == id)
            }
        }
    }
}


