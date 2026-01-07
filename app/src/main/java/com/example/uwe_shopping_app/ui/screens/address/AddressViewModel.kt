@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.example.uwe_shopping_app.ui.screens.address

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.AddressEntity
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.address.AddressType
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddressViewModel(application: Application) : AndroidViewModel(application) {

    private val addressDao = App.db.addressDao()
    private val sessionManager = SessionManager(application)

    private val currentUserId = sessionManager.userId

    // Get address list from databse with userId
    val addresses: StateFlow<List<AddressUiModel>> = currentUserId
        .flatMapLatest { userId ->
            if (userId != null) {
                addressDao.getAddressesByUserId(userId).map { entities ->
                    entities.map { it.toUiModel() }
                }
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectAddress(id: Int) {
        viewModelScope.launch {
            val userId = currentUserId.first() ?: return@launch
            addressDao.clearDefaultAddress(userId)
            addressDao.setDefaultAddress(id)
        }
    }

    fun addOrUpdateAddress(uiModel: AddressUiModel) {
        viewModelScope.launch {
            val userId = currentUserId.first() ?: return@launch
            val entity = uiModel.toEntity(userId)
            addressDao.insertAddress(entity)
        }
    }

    fun deleteAddress(uiModel: AddressUiModel) {
        viewModelScope.launch {
            val userId = currentUserId.first() ?: return@launch
            addressDao.deleteAddress(uiModel.toEntity(userId))
        }
    }

    // --- Mappers ---
    private fun AddressEntity.toUiModel() = AddressUiModel(
        id = id,
        title = type,
        recipient = recipient,
        addressLine = addressLine,
        district = district,
        city = city,
        phoneNumber = phoneNumber,
        type = if (type == "HOME") AddressType.HOME else AddressType.OFFICE,
        isSelected = isDefault
    )

    private fun AddressUiModel.toEntity(userId: Int) = AddressEntity(
        id = id,
        userId = userId,
        recipient = recipient,
        addressLine = addressLine,
        district = district,
        city = city,
        phoneNumber = phoneNumber,
        type = if (type == AddressType.HOME) "HOME" else "OFFICE",
        isDefault = isSelected
    )
}
