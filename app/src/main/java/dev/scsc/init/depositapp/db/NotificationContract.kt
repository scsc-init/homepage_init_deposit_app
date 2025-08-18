package dev.scsc.init.depositapp.db

import android.provider.BaseColumns

object NotificationContract {
    object NotificationEntry : BaseColumns {
        const val TABLE_NAME = "notification"
        const val COLUMN_NAME_PACKAGE_NAME = "package_name"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_TEXT = "text"
        const val COLUMN_NAME_POST_TIME = "post_time"
        const val COLUMN_NAME_AMOUNT = "amount"
        const val COLUMN_NAME_DEPOSIT_NAME = "deposit_name"
        const val COLUMN_NAME_RESULT_CODE = "result_code"
    }
}
