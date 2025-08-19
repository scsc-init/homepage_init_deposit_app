package dev.scsc.init.depositapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.scsc.init.depositapp.model.NotificationDTO
import dev.scsc.init.depositapp.util.Util.Companion.convertTimestampToISOString

@Composable
fun NotificationView(notifs: List<NotificationDTO>, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(12.dp)) {
        LazyColumn {
            items(notifs, key = { it.postTime }) { notif ->
                Text("${notif.amount}, ${convertTimestampToISOString(notif.postTime)}, ${notif.depositName}, ${notif.resultCode}")
            }
        }
    }
}
