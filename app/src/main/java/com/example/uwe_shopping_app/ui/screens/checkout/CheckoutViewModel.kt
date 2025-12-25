package com.example.uwe_shopping_app.ui.screens.checkout

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ShippingMethod(
    val id: String,
    val name: String,
    val price: Double,
    val description: String
)

data class CheckoutUiState(
    val firstName: String = "",
    val lastName: String = "",
    val streetName: String = "",
    val phoneNumber: String = "",
    val selectedShippingMethod: String = "free",
    val shippingLabel: String = "Free shipping",
    val shippingPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val couponCode: String = "", // BỔ SUNG
    val copyBillingAddress: Boolean = false // BỔ SUNG
)

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    
    private val addressDao = App.db.addressDao()
    private val sessionManager = SessionManager(application)
    
    var uiState by mutableStateOf(CheckoutUiState())
        private set

    init {
        observeDefaultAddress()
    }

    private fun observeDefaultAddress() {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            addressDao.getAddressesByUserId(userId).collectLatest { addresses ->
                val defaultAddress = addresses.find { it.isDefault } ?: addresses.firstOrNull()
                
                defaultAddress?.let {
                    val names = it.recipient.split(" ")
                    uiState = uiState.copy(
                        firstName = names.firstOrNull() ?: "",
                        lastName = if (names.size > 1) names.drop(1).joinToString(" ") else "",
                        streetName = it.addressLine,
                        phoneNumber = it.phoneNumber
                    )
                }
            }
        }
    }

    fun updateTotalPrice(price: Double) {
        uiState = uiState.copy(totalPrice = price)
    }
    
    val shippingMethods = listOf(
        ShippingMethod("free", "Free Delivery to home", 0.0, "Delivery from 3 to 7 business days"),
        ShippingMethod("standard", "$ 5.0 Delivery to home", 5.0, "Delivery from 4 to 6 business days"),
        ShippingMethod("fast", "$ 9.90 Fast Delivery", 9.90, "Delivery from 2 to 3 business days")
    )

    fun selectShippingMethod(methodId: String) {
        val method = shippingMethods.find { it.id == methodId }
        val price = method?.price ?: 0.0
        val label = method?.name ?: "Free shipping"
        uiState = uiState.copy(
            selectedShippingMethod = methodId, 
            shippingPrice = price,
            shippingLabel = label
        )
    }
    
    fun updateFromAddress(address: AddressUiModel) {
    }

    fun updateCouponCode(value: String) {
        uiState = uiState.copy(couponCode = value)
    }

    fun validateCouponCode() {
        // Có thể thêm logic xử lý mã giảm giá ở đây
    }
    
    fun toggleCopyBillingAddress() {
        uiState = uiState.copy(copyBillingAddress = !uiState.copyBillingAddress)
    }
}
