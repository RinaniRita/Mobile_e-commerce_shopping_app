package com.example.uwe_shopping_app.ui.screens.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ShippingMethod(
    val id: String,
    val name: String,
    val price: Double,
    val description: String
)

data class CheckoutUiState(
    // Form fields
    val firstName: String = "",
    val lastName: String = "",
    val country: String = "",
    val streetName: String = "",
    val city: String = "",
    val stateProvince: String = "",
    val zipCode: String = "",
    val phoneNumber: String = "",
    
    // Validation errors
    val lastNameError: String? = "Field is required",
    val countryError: String? = null,
    val streetNameError: String? = null,
    val cityError: String? = null,
    val zipCodeError: String? = null,
    val phoneNumberError: String? = null,
    
    // Shipping method
    val selectedShippingMethod: String = "free",
    
    // Coupon code
    val couponCode: String = "",
    
    // Billing address
    val copyBillingAddress: Boolean = false
)

class CheckoutViewModel : ViewModel() {
    
    var uiState by mutableStateOf(CheckoutUiState())
        private set
    
    val shippingMethods = listOf(
        ShippingMethod(
            id = "free",
            name = "Free Delivery to home",
            price = 0.0,
            description = "Delivery from 3 to 7 business days"
        ),
        ShippingMethod(
            id = "standard",
            name = "$ 9.90 Delivery to home",
            price = 9.90,
            description = "Delivery from 4 to 6 business days"
        ),
        ShippingMethod(
            id = "fast",
            name = "$ 9.90 Fast Delivery",
            price = 9.90,
            description = "Delivery from 2 to 3 business days"
        )
    )
    
    fun updateFirstName(value: String) {
        uiState = uiState.copy(firstName = value)
    }
    
    fun updateLastName(value: String) {
        val error = if (value.isBlank()) "Field is required" else null
        uiState = uiState.copy(lastName = value, lastNameError = error)
    }
    
    fun updateCountry(value: String) {
        val error = if (value.isBlank()) "Field is required" else null
        uiState = uiState.copy(country = value, countryError = error)
    }
    
    fun updateStreetName(value: String) {
        val error = if (value.isBlank()) "Field is required" else null
        uiState = uiState.copy(streetName = value, streetNameError = error)
    }
    
    fun updateCity(value: String) {
        val error = if (value.isBlank()) "Field is required" else null
        uiState = uiState.copy(city = value, cityError = error)
    }
    
    fun updateStateProvince(value: String) {
        uiState = uiState.copy(stateProvince = value)
    }
    
    fun updateZipCode(value: String) {
        val error = if (value.isBlank()) "Field is required" else null
        uiState = uiState.copy(zipCode = value, zipCodeError = error)
    }
    
    fun updatePhoneNumber(value: String) {
        val error = if (value.isBlank()) "Field is required" else null
        uiState = uiState.copy(phoneNumber = value, phoneNumberError = error)
    }
    
    fun selectShippingMethod(methodId: String) {
        uiState = uiState.copy(selectedShippingMethod = methodId)
    }
    
    fun updateCouponCode(value: String) {
        uiState = uiState.copy(couponCode = value)
    }
    
    fun validateCouponCode() {
        // Frontend only - just clear the field or show success
        // In real app, this would call backend API
    }
    
    fun toggleCopyBillingAddress() {
        uiState = uiState.copy(copyBillingAddress = !uiState.copyBillingAddress)
    }
    
    fun validateForm(): Boolean {
        var isValid = true
        val errors = mutableMapOf<String, String?>()
        
        if (uiState.lastName.isBlank()) {
            errors["lastName"] = "Field is required"
            isValid = false
        } else {
            errors["lastName"] = null
        }
        
        if (uiState.country.isBlank()) {
            errors["country"] = "Field is required"
            isValid = false
        } else {
            errors["country"] = null
        }
        
        if (uiState.streetName.isBlank()) {
            errors["streetName"] = "Field is required"
            isValid = false
        } else {
            errors["streetName"] = null
        }
        
        if (uiState.city.isBlank()) {
            errors["city"] = "Field is required"
            isValid = false
        } else {
            errors["city"] = null
        }
        
        if (uiState.zipCode.isBlank()) {
            errors["zipCode"] = "Field is required"
            isValid = false
        } else {
            errors["zipCode"] = null
        }
        
        if (uiState.phoneNumber.isBlank()) {
            errors["phoneNumber"] = "Field is required"
            isValid = false
        } else {
            errors["phoneNumber"] = null
        }
        
        uiState = uiState.copy(
            lastNameError = errors["lastName"],
            countryError = errors["country"],
            streetNameError = errors["streetName"],
            cityError = errors["city"],
            zipCodeError = errors["zipCode"],
            phoneNumberError = errors["phoneNumber"]
        )
        
        return isValid
    }
}

