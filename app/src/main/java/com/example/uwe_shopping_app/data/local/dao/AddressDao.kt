package com.example.uwe_shopping_app.data.local.dao

import androidx.room.*
import com.example.uwe_shopping_app.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM addresses WHERE userId = :userId")
    fun getAddressesByUserId(userId: Int): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultAddress(userId: Int)

    @Query("UPDATE addresses SET isDefault = 1 WHERE id = :addressId")
    suspend fun setDefaultAddress(addressId: Int)
}
