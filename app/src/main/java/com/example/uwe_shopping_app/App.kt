package com.example.uwe_shopping_app

import android.app.Application
import androidx.room.Room
import com.example.uwe_shopping_app.data.local.database.AppDatabase

class App : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "shop_db"
        ).build()
    }
}
