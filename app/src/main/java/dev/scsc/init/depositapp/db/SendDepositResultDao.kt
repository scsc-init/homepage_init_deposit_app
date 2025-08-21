package dev.scsc.init.depositapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SendDepositResultDao {
    @Insert
    fun insert(result: SendDepositResult)

    @Query("SELECT * FROM send_deposit_result ORDER BY id DESC")
    fun getAll(): List<SendDepositResult>

    @Query("DELETE FROM send_deposit_result")
    fun deleteAll()

    @Delete
    fun delete(result: SendDepositResult)

    @Query("DELETE FROM send_deposit_result WHERE result_code=200")
    fun deleteSucceeded()

    @Query("DELETE FROM send_deposit_result WHERE result_code!=200")
    fun deleteFailed()
}
