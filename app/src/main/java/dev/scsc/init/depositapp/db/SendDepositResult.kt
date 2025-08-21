package dev.scsc.init.depositapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "send_deposit_result")
data class SendDepositResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "result_code")
    val resultCode: Int,

    @ColumnInfo(name = "result_msg")
    val resultMsg: String,

    @ColumnInfo(name = "deposit_time")
    val depositTime: String,

    @ColumnInfo(name = "deposit_name")
    val depositName: String,

    @ColumnInfo(name = "amount")
    val amount: Long
)
