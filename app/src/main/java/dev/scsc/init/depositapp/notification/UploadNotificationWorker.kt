package dev.scsc.init.depositapp.notification

import android.content.ContentValues
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.scsc.init.depositapp.db.NotificationContract
import dev.scsc.init.depositapp.db.NotificationReaderDbHelper

class UploadNotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Retrieve data passed from the service
        val packageName: String? = inputData.getString(KEY_PACKAGE_NAME)
        val title: String? = inputData.getString(KEY_TITLE)
        val text: String? = inputData.getString(KEY_TEXT)
        val postTime: Long = inputData.getLong(KEY_POST_TIME, 0)

        if (packageName.isNullOrBlank() || title.isNullOrBlank() || text.isNullOrBlank() || postTime == 0L) {
            return Result.failure()
        }

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(NotificationContract.NotificationEntry.COLUMN_NAME_PACKAGE_NAME, packageName)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_TITLE, title)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_TEXT, text)
            put(NotificationContract.NotificationEntry.COLUMN_NAME_POST_TIME, postTime)
        }

        // Perform the network request here.
        // For example, using a library like Retrofit or HttpUrlConnection.

        return try {
            val dbHelper = NotificationReaderDbHelper(applicationContext)
            try {
                dbHelper.writableDatabase.use { db ->
                    // Insert the new row, returning the primary key value of the new row
                    val newRowId =
                        db.insert(NotificationContract.NotificationEntry.TABLE_NAME, null, values)
                    if (newRowId == -1L) {
                        Result.failure()
                    } else {
                        Result.success()
                    }
                }
            } finally {
                dbHelper.close()
            }
        } catch (_: android.database.sqlite.SQLiteDatabaseLockedException) {
            Result.failure()
        } catch (_: android.database.sqlite.SQLiteException) {
            Result.failure()
        }
    }

    companion object {
        const val KEY_PACKAGE_NAME = "package_name"
        const val KEY_TITLE = "title"
        const val KEY_TEXT = "text"
        const val KEY_POST_TIME = "post_time"
    }
}
