package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.UserEntity

class UserRepository {

    private val userDao = App.db.userDao()

    /* ---------- Register result ---------- */
    sealed class RegisterResult {
        object Success : RegisterResult()
        object EmailExists : RegisterResult()
        object PhoneExists : RegisterResult()
    }

    /* ---------- Registration ---------- */
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

        userDao.insertUser(user)
        return RegisterResult.Success
    }

    /* ---------- Login ---------- */
    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }

    /* ---------- Existing helpers ---------- */
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserByPhone(phone: String): UserEntity? {
        return userDao.getUserByPhone(phone)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }
}
