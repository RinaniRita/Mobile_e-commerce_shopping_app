package com.example.uwe_shopping_app.ui.screens.voucher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.VoucherEntity
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VoucherViewModel(application: Application) : AndroidViewModel(application) {
    private val voucherDao = App.db.voucherDao()
    private val sessionManager = SessionManager(application)

    private val currentUserId = sessionManager.userId

    val vouchers: StateFlow<List<VoucherEntity>> = currentUserId
        .flatMapLatest { userId ->
            if (userId != null) {
                voucherDao.getVouchersByUser(userId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun canUseVoucher(voucher: VoucherEntity): Boolean {
        return voucher.usageCount < voucher.maxUsage
    }
}
