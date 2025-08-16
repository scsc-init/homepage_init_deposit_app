package dev.scsc.init.depositapp.notification

import android.content.ContentValues
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.scsc.init.depositapp.db.NotificationContract
import dev.scsc.init.depositapp.db.NotificationReaderDbHelper

class UploadNotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        // Retrieve data passed from the service
        val packageName: String? = inputData.getString("package_name")
        val title: String? = inputData.getString("title")
        val text: String? = inputData.getString("text")
        val postTime: Long = inputData.getLong("post_time", 0)

        if (packageName == null || title == null || text == null) {
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
                        Result.retry()
                    } else {
                        Result.success()
                    }
                }
            } finally {
                dbHelper.close()
            }
        } catch (_: android.database.sqlite.SQLiteDatabaseLockedException) {
            Result.retry()
        } catch (_: android.database.sqlite.SQLiteException) {
            Result.failure()
        }
    }
}
