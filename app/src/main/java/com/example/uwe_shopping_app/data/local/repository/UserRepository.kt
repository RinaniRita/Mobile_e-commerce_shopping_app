package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.UserEntity

class UserRepository {

    private val userDao = App.db.userDao()

    suspend fun registerUser(name: String, email: String, password: String, phone: String): Boolean {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) return false

        val user = UserEntity(name = name, email = email, password = password, phone = phone)
        userDao.insertUser(user)
        return true
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }
    
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }
}
