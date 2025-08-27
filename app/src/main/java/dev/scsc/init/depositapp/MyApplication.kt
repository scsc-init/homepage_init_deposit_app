package dev.scsc.init.depositapp

import android.app.Application
import dev.scsc.init.depositapp.db.NotificationDatabase
import dev.scsc.init.depositapp.db.NotificationRepository

class MyApplication : Application() {
    val database: NotificationDatabase by lazy {
        NotificationDatabase.getDatabase(this)
    }
    val repository: NotificationRepository by lazy {
        NotificationRepository(this, database)
    }
}
