package com.example.uwe_shopping_app.data.local.dao

import androidx.room.*
import com.example.uwe_shopping_app.data.local.entity.VoucherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VoucherDao {
    @Query("SELECT * FROM vouchers WHERE userId = :userId")
    fun getVouchersByUser(userId: Int): Flow<List<VoucherEntity>>

    @Query("SELECT * FROM vouchers WHERE userId = :userId AND code = :code LIMIT 1")
    suspend fun getVoucherByCode(userId: Int, code: String): VoucherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoucher(voucher: VoucherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVouchers(vouchers: List<VoucherEntity>)

    @Update
    suspend fun updateVoucher(voucher: VoucherEntity)

    @Query("UPDATE vouchers SET usageCount = usageCount + 1 WHERE id = :voucherId")
    suspend fun incrementUsage(voucherId: Int)
}
