package dev.scsc.init.depositapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResultView(
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = viewModel(factory = ResultViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.onDeleteSucceededButtonClick() }) { Text("Delete Succeeded Data") }
            Button(onClick = { viewModel.onDeleteFailedButtonClick() }) { Text("Delete Failed Data") }
        }
        LazyColumn {
            items(uiState, key = { it.id }) { result ->
                Text("${result.amount}, ${result.depositTime}, ${result.depositName}, ${result.resultCode}")
            }
        }
    }
}
