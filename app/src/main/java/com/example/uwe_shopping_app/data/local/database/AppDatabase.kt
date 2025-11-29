package com.example.uwe_shopping_app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.uwe_shopping_app.data.local.dao.UserDao
import com.example.uwe_shopping_app.data.local.entity.UserEntity

@Database(
    entities = [

        UserEntity::class
    ],
    version = 2,           // bump version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}