package dev.scsc.init.depositapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import dev.scsc.init.depositapp.db.NotificationContract
import dev.scsc.init.depositapp.db.NotificationReaderDbHelper
import dev.scsc.init.depositapp.model.NotificationDTO
import dev.scsc.init.depositapp.ui.NotificationView
import dev.scsc.init.depositapp.ui.theme.DepositAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!permissionGranted()) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }

        val dbHelper = NotificationReaderDbHelper(this)

        val notifs = mutableListOf<NotificationDTO>()
        dbHelper.readableDatabase.use { db ->
            db.query(
                NotificationContract.NotificationEntry.TABLE_NAME,
                null, // columns (null => all)
                null, // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                "${NotificationContract.NotificationEntry.COLUMN_NAME_POST_TIME} DESC" // The sort order
            ).use { cursor ->
                val idxPkg =
                    cursor.getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_PACKAGE_NAME)
                val idxTitle =
                    cursor.getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_TITLE)
                val idxText =
                    cursor.getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_TEXT)
                val idxPost =
                    cursor.getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_POST_TIME)
                val idxAmount =
                    cursor.getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_AMOUNT)
                val idxDepositName =
                    cursor.getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_DEPOSIT_NAME)
                val idxResultCode =
                    cursor.getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_RESULT_CODE)
                while (cursor.moveToNext()) {
                    val packageName = cursor.getString(idxPkg)
                    val title = cursor.getString(idxTitle)
                    val text = cursor.getString(idxText)
                    val postTime = cursor.getLong(idxPost)
                    val amount = cursor.getLongOrNull(idxAmount)
                    val depositName = cursor.getStringOrNull(idxDepositName)
                    val resultCode = cursor.getIntOrNull(idxResultCode)
                    notifs.add(
                        NotificationDTO(
                            packageName,
                            title,
                            text,
                            postTime,
                            amount,
                            depositName,
                            resultCode
                        )
                    )
                }
            }
        }
        dbHelper.close()

        setContent {
            DepositAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotificationView(
                        notifs = notifs,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun permissionGranted(): Boolean {
        val sets = NotificationManagerCompat.getEnabledListenerPackages(this)
        return sets.contains(packageName)
    }
}
