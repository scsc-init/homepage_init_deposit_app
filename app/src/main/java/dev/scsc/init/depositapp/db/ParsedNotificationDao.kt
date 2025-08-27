package dev.scsc.init.depositapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ParsedNotificationDao {
    @Insert
    suspend fun insert(notification: ParsedNotification)

    @Query("SELECT * FROM parsed_notification ORDER BY id DESC")
    suspend fun getAll(): List<ParsedNotification>

    @Query("DELETE FROM parsed_notification")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(notif: ParsedNotification)
}
