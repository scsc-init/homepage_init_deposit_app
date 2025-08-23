package dev.scsc.init.depositapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RawNotificationDao {
    @Insert
    suspend fun insert(notification: RawNotification)

    @Query("SELECT * FROM raw_notification ORDER BY id DESC")
    suspend fun getAll(): List<RawNotification>

    @Query("DELETE FROM raw_notification")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(notif: RawNotification)
}
