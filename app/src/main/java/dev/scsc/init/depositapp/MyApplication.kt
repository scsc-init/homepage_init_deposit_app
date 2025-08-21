package dev.scsc.init.depositapp

import android.app.Application
import dev.scsc.init.depositapp.db.NotificationDatabase
import dev.scsc.init.depositapp.db.NotificationRepository

class MyApplication : Application() {
    lateinit var repository: NotificationRepository

    override fun onCreate() {
        super.onCreate()
        val db = NotificationDatabase.getDatabase(applicationContext)
        repository = NotificationRepository(
            applicationContext,
            db.rawNotificationDao(),
            db.processedNotificationDao(),
            db.sendDepositResultDao()
        )
    }
}
