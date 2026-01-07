package com.example.uwe_shopping_app.ui.screens.checkout

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.VoucherEntity
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.data.remote.NominatimApiService
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.*

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
    val district: String = "",
    val city: String = "",
    val phoneNumber: String = "",
    val selectedShippingMethod: String = "free",
    val shippingLabel: String = "Free shipping",
    
    // Base shipping price (method)
    val methodPrice: Double = 0.0,
    // Distance shipping price
    val distancePrice: Double = 0.0,
    
    val productPrice: Double = 0.0,
    val couponCode: String = "",
    val copyBillingAddress: Boolean = false,
    
    // Voucher states
    val appliedVoucher: VoucherEntity? = null,
    val voucherError: String? = null,
    
    // States cho API to calculate shipping
    val isCalculating: Boolean = false,
    val distanceKm: Double? = null,
    val calculationError: String? = null
) {
    // CALCULATING PRICE
    
    val itemDiscount: Double
        get() {
            val v = appliedVoucher ?: return 0.0
            if (v.target != "PRODUCT") return 0.0
            return if (v.discountType == "PERCENTAGE") {
                (productPrice * v.discountValue / 100.0)
            } else {
                v.discountValue.toDouble()
            }
        }

    val shippingDiscount: Double
        get() {
            val v = appliedVoucher ?: return 0.0
            if (v.target != "SHIPPING") return 0.0
            val totalShip = methodPrice + distancePrice
            val discount = if (v.discountType == "PERCENTAGE") {
                (totalShip * v.discountValue / 100.0)
            } else {
                v.discountValue.toDouble()
            }
            return min(discount, totalShip) // Không giảm quá tiền ship
        }

    val finalProductPrice: Double get() = productPrice - itemDiscount
    val finalShippingPrice: Double get() = (methodPrice + distancePrice) - shippingDiscount
    val grandTotal: Double get() = finalProductPrice + finalShippingPrice
}

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    
    private val addressDao = App.db.addressDao()
    private val voucherDao = App.db.voucherDao()
    private val sessionManager = SessionManager(application)
    
    private val shopLat = 20.9626
    private val shopLon = 105.7460

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nominatim.openstreetmap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(NominatimApiService::class.java)

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
                defaultAddress?.let { updateFromAddress(it.toUiModel()) }
            }
        }
    }

    fun updateFromAddress(address: AddressUiModel) {
        val names = address.recipient.split(" ")
        uiState = uiState.copy(
            firstName = names.firstOrNull() ?: "",
            lastName = if (names.size > 1) names.drop(1).joinToString(" ") else "",
            streetName = address.addressLine,
            district = address.district,
            city = address.city,
            phoneNumber = address.phoneNumber,
            distanceKm = null // Reset to re verify
        )
    }

    fun verifyAddressAndCalculateFee() {
        val query = "${uiState.streetName}, ${uiState.district}, ${uiState.city}, Vietnam"
        val fallback = "${uiState.district}, ${uiState.city}, Vietnam"

        viewModelScope.launch {
            uiState = uiState.copy(isCalculating = true, calculationError = null)
            try {
                var results = apiService.searchAddress(query)
                if (results.isEmpty()) results = apiService.searchAddress(fallback)

                if (results.isNotEmpty()) {
                    val distance = calculateDistance(shopLat, shopLon, results[0].lat.toDouble(), results[0].lon.toDouble())
                    val distPrice = when {
                        distance < 5.0 -> 0.0
                        distance < 15.0 -> 2.0
                        else -> 5.0
                    }
                    uiState = uiState.copy(distanceKm = distance, distancePrice = distPrice, isCalculating = false)
                } else {
                    uiState = uiState.copy(isCalculating = false, calculationError = "Address not found.")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isCalculating = false, calculationError = "Network error.")
            }
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        return r * (2 * atan2(sqrt(a), sqrt(1 - a)))
    }

    fun validateCouponCode() {
        val code = uiState.couponCode.trim()
        if (code.isEmpty()) {
            uiState = uiState.copy(appliedVoucher = null, voucherError = null)
            return
        }

        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val voucher = voucherDao.getVoucherByCode(userId, code)

            if (voucher == null) {
                uiState = uiState.copy(voucherError = "Invalid voucher code", appliedVoucher = null)
            } else if (voucher.usageCount >= voucher.maxUsage) {
                uiState = uiState.copy(voucherError = "Voucher usage limit reached", appliedVoucher = null)
            } else {
                uiState = uiState.copy(appliedVoucher = voucher, voucherError = null)
            }
        }
    }

    fun updateTotalPrice(price: Double) { uiState = uiState.copy(productPrice = price) }
    fun updateCouponCode(value: String) { uiState = uiState.copy(couponCode = value) }
    fun toggleCopyBillingAddress() { uiState = uiState.copy(copyBillingAddress = !uiState.copyBillingAddress) }

    val shippingMethods = listOf(
        ShippingMethod("free", "Free Delivery (Slow)", 0.0, "7-10 days"),
        ShippingMethod("standard", "Standard Delivery", 5.0, "3-5 days"),
        ShippingMethod("fast", "Fast Delivery", 10.0, "1-2 days")
    )

    fun selectShippingMethod(methodId: String) {
        val method = shippingMethods.find { it.id == methodId }
        uiState = uiState.copy(
            selectedShippingMethod = methodId,
            methodPrice = method?.price ?: 0.0,
            shippingLabel = method?.name ?: ""
        )
    }

    private fun com.example.uwe_shopping_app.data.local.entity.AddressEntity.toUiModel() = AddressUiModel(
        id = id, title = type, recipient = recipient, addressLine = addressLine,
        district = district, city = city, phoneNumber = phoneNumber,
        type = if (type == "HOME") com.example.uwe_shopping_app.ui.components.address.AddressType.HOME else com.example.uwe_shopping_app.ui.components.address.AddressType.OFFICE,
        isSelected = isDefault
    )
}
