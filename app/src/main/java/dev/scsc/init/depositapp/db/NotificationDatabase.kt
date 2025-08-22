package dev.scsc.init.depositapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [RawNotification::class, ParsedNotification::class, SendDepositResult::class],
    version = 2,
    exportSchema = true
)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun rawNotificationDao(): RawNotificationDao
    abstract fun parsedNotificationDao(): ParsedNotificationDao
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
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE INDEX `index_send_deposit_result_result_code` ON `send_deposit_result` (`result_code`)"
        )
    }
}
