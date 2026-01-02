package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.UserEntity
import com.example.uwe_shopping_app.data.local.entity.VoucherEntity
import com.example.uwe_shopping_app.data.local.session.SessionManager

class UserRepository(
    private val sessionManager: SessionManager
) {

    private val userDao = App.db.userDao()
    private val voucherDao = App.db.voucherDao()
    private val notificationRepo = NotificationRepository(sessionManager)

    sealed class RegisterResult {
        object Success : RegisterResult()
        object EmailExists : RegisterResult()
        object PhoneExists : RegisterResult()
    }

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        phone: String
    ): RegisterResult {

        if (userDao.getUserByEmail(email) != null) {
            return RegisterResult.EmailExists
        }

        if (userDao.getUserByPhone(phone) != null) {
            return RegisterResult.PhoneExists
        }

        val user = UserEntity(
            name = name,
            email = email,
            password = password,
            phone = phone
        )

        val userId = userDao.insertUser(user).toInt()
        
        // AUTO CREATE 4 VOUCHERS WHEN LOGGED IN
        val initialVouchers = listOf(
            VoucherEntity(
                userId = userId,
                code = "fridaysale",
                title = "Black Friday",
                description = "Sale off 50%",
                discountValue = 50,
                discountType = "PERCENTAGE",
                target = "PRODUCT",
                expiryDay = 20,
                expiryMonth = "Dec"
            ),
            VoucherEntity(
                userId = userId,
                code = "holiday30",
                title = "Holiday Sale",
                description = "Sale off 30%",
                discountValue = 30,
                discountType = "PERCENTAGE",
                target = "PRODUCT",
                expiryDay = 22,
                expiryMonth = "Dec"
            ),
            VoucherEntity(
                userId = userId,
                code = "welcome",
                title = "First order",
                description = "20% off your first order",
                discountValue = 20,
                discountType = "PERCENTAGE",
                target = "PRODUCT",
                expiryDay = 28,
                expiryMonth = "Dec"
            ),
            // VOUCHER LOẠI 2: GIẢM PHÍ SHIP
            VoucherEntity(
                userId = userId,
                code = "FREESHIP2",
                title = "Shipping Discount",
                description = "Save $2.0 on shipping fee",
                discountValue = 2,
                discountType = "FIXED",
                target = "SHIPPING",
                expiryDay = 31,
                expiryMonth = "Dec"
            )
        )
        voucherDao.insertVouchers(initialVouchers)

        notificationRepo.notify(
            userId = userId,
            title = "Good morning! Get vouchers",
            message = "You received ${initialVouchers.size} new vouchers. Use them before they expire!",
            type = "VOUCHER"
        )

        return RegisterResult.Success
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }

    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
}
