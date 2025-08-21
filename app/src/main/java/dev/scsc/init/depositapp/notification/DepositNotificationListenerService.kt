package dev.scsc.init.depositapp.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dev.scsc.init.depositapp.MyApplication
import dev.scsc.init.depositapp.R
import dev.scsc.init.depositapp.db.NotificationRepository
import dev.scsc.init.depositapp.db.RawNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepositNotificationListenerService : NotificationListenerService() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var repository: NotificationRepository
    override fun onCreate() {
        super.onCreate()
        repository = (application as MyApplication).repository
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = (extras.getCharSequence(Notification.EXTRA_TITLE) ?: "").toString()
        val text = (extras.getCharSequence(Notification.EXTRA_TEXT)
            ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
            ?: "").toString()
        val postTime = sbn.postTime

        if (packageName != getString(R.string.bank_package_name)) return
        if (title.isBlank() && text.isBlank()) return
        if (packageName != getString(R.string.bank_package_name)) return
        val rawNotification = RawNotification(title = title, content = text, timestamp = postTime)

        scope.launch {
            repository.insertRawNotification(rawNotification)
        }
    }
}
