package dev.scsc.init.depositapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SendDepositResultDao {
    @Insert
    suspend fun insert(result: SendDepositResult)

    @Query("SELECT * FROM send_deposit_result ORDER BY id DESC")
    suspend fun getAll(): List<SendDepositResult>

    @Query("DELETE FROM send_deposit_result")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(result: SendDepositResult)

    @Query("DELETE FROM send_deposit_result WHERE result_code=200")
    suspend fun deleteSucceeded()

    @Query("DELETE FROM send_deposit_result WHERE result_code!=200")
    suspend fun deleteFailed()
}
