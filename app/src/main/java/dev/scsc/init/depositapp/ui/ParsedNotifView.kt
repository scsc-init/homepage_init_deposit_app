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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ParsedNotifView(modifier: Modifier = Modifier) {
    val viewModel: ParsedNotifViewModel = viewModel(factory = ParsedNotifViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refreshState()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.onSendButtonClick() }) { Text("Send Data") }
        }
        LazyColumn {
            items(uiState, key = { it.id }) { processedNotif ->
                Text("${processedNotif.amount}, ${processedNotif.depositName}, ${processedNotif.depositTime}")
            }
        }
    }
}
