package com.example.uwe_shopping_app.ui.screens.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.uwe_shopping_app.ui.components.order.OrderStatus

data class OrderLineItem(
    val name: String,
    val quantity: Int,
    val price: Double
)

enum class OrderInfoScenario {
    DELIVERED_SAMPLE,
    ON_THE_WAY_SAMPLE
}

data class OrderInfoUiState(
    val orderId: String = "",
    val trackingNumber: String = "",
    val deliveryAddress: String = "",
    val items: List<OrderLineItem> = emptyList(),
    val subtotal: Double = 0.0,
    val shipping: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING
)

class OrderInfoViewModel : ViewModel() {

    var uiState by mutableStateOf(OrderInfoUiState())
        private set

    /**
     * For now this only loads static/sample data for the two UI scenarios.
     * No backend / database calls are performed here yet.
     */
    fun loadScenario(scenario: OrderInfoScenario) {
        uiState = when (scenario) {
            OrderInfoScenario.DELIVERED_SAMPLE -> OrderInfoUiState(
                orderId = "1514",
                trackingNumber = "IK987362341",
                deliveryAddress = "SBI Building, Software Park",
                items = listOf(
                    OrderLineItem("Maxi Dress", quantity = 1, price = 68.0),
                    OrderLineItem("Linen Dress", quantity = 1, price = 52.0)
                ),
                subtotal = 120.0,
                shipping = 0.0,
                status = OrderStatus.DELIVERED
            )

            OrderInfoScenario.ON_THE_WAY_SAMPLE -> OrderInfoUiState(
                orderId = "1524",
                trackingNumber = "IK287368838",
                deliveryAddress = "SBI Building, Software Park",
                items = listOf(
                    OrderLineItem("Sportwear Set", quantity = 1, price = 80.0),
                    OrderLineItem("Cotton T-shirt", quantity = 1, price = 30.0)
                ),
                subtotal = 110.0,
                shipping = 0.0,
                status = OrderStatus.ON_THE_WAY
            )
        }
    }
}
