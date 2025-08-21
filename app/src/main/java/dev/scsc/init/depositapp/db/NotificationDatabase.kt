package dev.scsc.init.depositapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RawNotification::class, ParsedNotification::class, SendDepositResult::class],
    version = 1,
    exportSchema = true
)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun rawNotificationDao(): RawNotificationDao
    abstract fun processedNotificationDao(): ParsedNotificationDao
    abstract fun sendDepositResultDao(): SendDepositResultDao

    companion object {
        @Volatile
        private var INSTANCE: NotificationDatabase? = null

        fun getDatabase(context: Context): NotificationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "notification_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
