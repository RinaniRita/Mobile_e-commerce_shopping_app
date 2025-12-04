package com.example.uwe_shopping_app.data.local.repository

import com.example.uwe_shopping_app.App
import com.example.uwe_shopping_app.data.local.entity.UserEntity

class UserRepository {

    private val userDao = App.db.userDao()

    suspend fun registerUser(name: String, email: String, password: String): Boolean {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) return false

        val user = UserEntity(name = name, email = email, password = password)
        userDao.insertUser(user)
        return true
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        return userDao.login(email, password) != null
    }
}   