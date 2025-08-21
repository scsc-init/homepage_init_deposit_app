package dev.scsc.init.depositapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ParsedNotificationDao {
    @Insert
    fun insert(notification: ParsedNotification)

    @Query("SELECT * FROM parsed_notification ORDER BY id DESC")
    fun getAll(): List<ParsedNotification>

    @Query("DELETE FROM parsed_notification")
    fun deleteAll()

    @Delete
    fun delete(notif: ParsedNotification)
}
