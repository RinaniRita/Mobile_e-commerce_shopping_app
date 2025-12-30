package com.example.uwe_shopping_app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.data.local.dao.*
import com.example.uwe_shopping_app.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CartEntity::class,
        CartItemEntity::class,
        AddressEntity::class,
        VoucherEntity::class,
        PaymentCardEntity::class,
        ProductReviewEntity::class,
        WishlistEntity::class,
        NotificationEntity::class,
    ],
    version = 6,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun cartDao(): CartDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun addressDao(): AddressDao
    abstract fun voucherDao(): VoucherDao
    abstract fun productReviewDao(): ProductReviewDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun notificationDao(): NotificationDao
    abstract fun paymentCardDao(): PaymentCardDao

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
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun populateDatabase(productDao: ProductDao) {
        val sampleProducts = listOf(
            ProductEntity(
                name = "Smartphone Ultra",
                description = "High-end smartphone with AI camera.",
                price = 999.0,
                imageResId = R.drawable.elec_phone,
                stock = 50,
                category = "Electronics"
            ),
            ProductEntity(
                name = "Laptop Pro 15",
                description = "Powerful laptop for professionals.",
                price = 1299.0,
                imageResId = R.drawable.elec_laptop,
                stock = 30,
                category = "Electronics"
            ),
            ProductEntity(
                name = "Headphones",
                description = "Noise cancelling.",
                price = 199.0,
                imageResId = R.drawable.elec_headphone,
                stock = 100,
                category = "Electronics"
            ),
            ProductEntity(
                name = "Smart Watch",
                description = "Fitness tracker.",
                price = 249.0,
                imageResId = R.drawable.elec_watch,
                stock = 75,
                category = "Electronics"
            ),
            ProductEntity(
                name = "T-Shirt",
                description = "Cotton t-shirt.",
                price = 29.0,
                imageResId = R.drawable.fash_tshirt,
                stock = 200,
                category = "Fashion"
            ),
            ProductEntity(
                name = "Jacket",
                description = "Denim jacket.",
                price = 89.0,
                imageResId = R.drawable.fash_jacket,
                stock = 60,
                category = "Fashion"
            ),
            ProductEntity(
                name = "Sneakers",
                description = "Running shoes.",
                price = 119.0,
                imageResId = R.drawable.fash_sneaker,
                stock = 45,
                category = "Fashion"
            ),
            ProductEntity(
                name = "Backpack",
                description = "Leather bag.",
                price = 149.0,
                imageResId = R.drawable.fash_backpack,
                stock = 25,
                category = "Fashion"
            ),
            ProductEntity(
                name = "Desk Lamp",
                description = "LED lamp.",
                price = 45.0,
                imageResId = R.drawable.home_lamp,
                stock = 80,
                category = "Home"
            ),
            ProductEntity(
                name = "Plant",
                description = "Succulent.",
                price = 15.0,
                imageResId = R.drawable.home_plant,
                stock = 150,
                category = "Home"
            ),
            ProductEntity(
                name = "Mug",
                description = "Coffee mug.",
                price = 12.0,
                imageResId = R.drawable.home_coffeemug,
                stock = 120,
                category = "Home"
            ),
            ProductEntity(
                name = "Sofa",
                description = "Comfort sofa.",
                price = 499.0,
                imageResId = R.drawable.home_sofa,
                stock = 10,
                category = "Home"
            ),
            ProductEntity(
                name = "Serum",
                description = "Face serum.",
                price = 35.0,
                imageResId = R.drawable.beauty_serum,
                stock = 90,
                category = "Beauty"
            ),
            ProductEntity(
                name = "Lipstick",
                description = "Red matte.",
                price = 22.0,
                imageResId = R.drawable.beauty_lipstick,
                stock = 110,
                category = "Beauty"
            ),
            ProductEntity(
                name = "Shampoo",
                description = "Organic.",
                price = 18.0,
                imageResId = R.drawable.beauty_shampoo,
                stock = 85,
                category = "Beauty"
            ),
            ProductEntity(
                name = "Perfume",
                description = "Luxury scent.",
                price = 85.0,
                imageResId = R.drawable.beauty_perfume,
                stock = 40,
                category = "Beauty"
            ),
            ProductEntity(
                name = "Yoga Mat",
                description = "Non-slip mat.",
                price = 30.0,
                imageResId = R.drawable.sport_yoga,
                stock = 70,
                category = "Sports"
            ),
            ProductEntity(
                name = "Dumbbell",
                description = "5kg set.",
                price = 55.0,
                imageResId = R.drawable.sport_dumbbell,
                stock = 35,
                category = "Sports"
            ),
            ProductEntity(
                name = "Bottle",
                description = "Water bottle.",
                price = 25.0,
                imageResId = R.drawable.sport_bottle,
                stock = 100,
                category = "Sports"
            ),
            ProductEntity(
                name = "Racket",
                description = "Tennis racket.",
                price = 120.0,
                imageResId = R.drawable.sport_racket,
                stock = 15,
                category = "Sports"
            )
        )
        productDao.insertAll(sampleProducts)
    }
}
