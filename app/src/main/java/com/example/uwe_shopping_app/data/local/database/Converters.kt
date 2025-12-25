package com.example.uwe_shopping_app.data.local.database

import androidx.room.TypeConverter
import com.example.uwe_shopping_app.ui.components.order.OrderStatus

class Converters {

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String {
        return status.name
    }

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus {
        return OrderStatus.valueOf(value)
    }
}
