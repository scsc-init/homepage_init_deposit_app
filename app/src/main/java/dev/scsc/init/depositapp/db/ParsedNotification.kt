package dev.scsc.init.depositapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parsed_notification")
data class ParsedNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "deposit_time")
    val depositTime: String,

    @ColumnInfo(name = "deposit_name")
    val depositName: String,

    @ColumnInfo(name = "amount")
    val amount: Long
)
