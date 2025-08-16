package dev.scsc.init.depositapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import dev.scsc.init.depositapp.db.NotificationContract.NotificationEntry


class NotificationReaderDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "notification.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${NotificationEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${NotificationEntry.COLUMN_NAME_PACKAGE_NAME} TEXT NOT NULL," +
                    "${NotificationEntry.COLUMN_NAME_TITLE} TEXT NOT NULL," +
                    "${NotificationEntry.COLUMN_NAME_TEXT} TEXT NOT NULL," +
                    "${NotificationEntry.COLUMN_NAME_POST_TIME} INT NOT NULL)"

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${NotificationEntry.TABLE_NAME}"
    }
}
