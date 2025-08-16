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
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            NotificationContract.NotificationEntry.TABLE_NAME,
            null, // The array of columns to return (pass null to get all)
            null, // The columns for the WHERE clause
            null, // The values for the WHERE clause
            null, // don't group the rows
            null, // don't filter by row groups
            "${NotificationContract.NotificationEntry.COLUMN_NAME_POST_TIME} DESC" // The sort order
        )

        val notifs = mutableListOf<NotificationDTO>()
        with(cursor) {
            while (moveToNext()) {
                val packageName =
                    getString(getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_PACKAGE_NAME))
                val title =
                    getString(getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_TITLE))
                val text =
                    getString(getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_TEXT))
                val postTime =
                    getLong(getColumnIndexOrThrow(NotificationContract.NotificationEntry.COLUMN_NAME_POST_TIME))
                notifs.add(NotificationDTO(packageName, title, text, postTime))
            }
        }
        cursor.close()

        db.close()
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
