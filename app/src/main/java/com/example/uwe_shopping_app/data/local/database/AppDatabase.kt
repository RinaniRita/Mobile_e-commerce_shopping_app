package com.example.uwe_shopping_app.data.local.database



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uwe_shopping_app.data.local.dao.CartDao
import com.example.uwe_shopping_app.data.local.dao.CartItemDao
import com.example.uwe_shopping_app.data.local.dao.OrderDao
import com.example.uwe_shopping_app.data.local.dao.OrderItemDao
import com.example.uwe_shopping_app.data.local.dao.ProductDao
import com.example.uwe_shopping_app.data.local.dao.UserDao
import com.example.uwe_shopping_app.data.local.entity.CartEntity
import com.example.uwe_shopping_app.data.local.entity.CartItemEntity
import com.example.uwe_shopping_app.data.local.entity.OrderEntity
import com.example.uwe_shopping_app.data.local.entity.OrderItemEntity
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CartEntity::class,
        CartItemEntity::class
    ],
    version = 3,  // Bump version vì thêm entities mới
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun cartDao(): CartDao
    abstract fun cartItemDao(): CartItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "uwe_shopping_db"
                )
                    .fallbackToDestructiveMigration()   //
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
