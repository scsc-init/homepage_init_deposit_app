package dev.scsc.init.depositapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RawNotificationDao {
    @Insert
    fun insert(notification: RawNotification)

    @Query("SELECT * FROM raw_notification ORDER BY id DESC")
    fun getAll(): List<RawNotification>

    @Query("DELETE FROM raw_notification")
    fun deleteAll()

    @Delete
    fun delete(notif: RawNotification)
}
