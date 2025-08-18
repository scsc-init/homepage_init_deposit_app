package dev.scsc.init.depositapp.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.scsc.init.depositapp.R

class DepositNotificationListenerService : NotificationListenerService() {
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
                        UploadNotificationWorker.KEY_PACKAGE_NAME to packageName,
                        UploadNotificationWorker.KEY_TITLE to title.take(1024),
                        UploadNotificationWorker.KEY_TEXT to text.take(4096),
                        UploadNotificationWorker.KEY_POST_TIME to postTime
                    )
                )
                .build()

        WorkManager.getInstance(applicationContext).enqueue(uploadRequest)
    }
}
