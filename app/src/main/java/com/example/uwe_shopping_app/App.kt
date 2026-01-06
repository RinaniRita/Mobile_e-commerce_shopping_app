package com.example.uwe_shopping_app

import android.app.Application
import androidx.room.Room
import com.example.uwe_shopping_app.data.local.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class App : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "uwe_shopping_db" //  MUST MATCH AppDatabase
        ).build()

        // Populate database once
        CoroutineScope(Dispatchers.IO).launch {
            val count = db.productDao().getProductCount()
            if (count == 0) {
                db.populateDatabase(db.productDao())
            }
        }
    }
}
