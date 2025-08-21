package dev.scsc.init.depositapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.scsc.init.depositapp.util.Util.Companion.convertTimestampToISOString

@Preview
@Composable
fun RawNotifView(modifier: Modifier = Modifier) {
    val viewModel: RawNotifViewModel = viewModel(factory = RawNotifViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshState()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.onParseButtonClick() }) { Text("Parse Data") }
        }
        LazyColumn {
            items(uiState, key = { it.id }) { rawNotif ->
                Text("${rawNotif.title}, ${rawNotif.content}, ${convertTimestampToISOString(rawNotif.timestamp)}")
            }
        }
    }
}
