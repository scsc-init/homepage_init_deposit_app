package dev.scsc.init.depositapp.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf

class DepositNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val title = sbn.notification.extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = sbn.notification.extras.getString(Notification.EXTRA_TEXT) ?: ""
        val postTime = sbn.postTime

        sendNotificationToServer(packageName, title, text, postTime)
    }

    private fun sendNotificationToServer(
        packageName: String,
        title: String,
        text: String,
        postTime: Long
    ) {
        val uploadRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadNotificationWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    workDataOf(
                        "package_name" to packageName,
                        "title" to title,
                        "text" to text,
                        "post_time" to postTime
                    )
                )
                .build()

        WorkManager.Companion.getInstance(applicationContext).enqueue(uploadRequest)
    }
}
