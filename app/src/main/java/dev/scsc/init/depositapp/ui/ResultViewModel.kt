package dev.scsc.init.depositapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.scsc.init.depositapp.MyApplication
import dev.scsc.init.depositapp.db.NotificationRepository
import dev.scsc.init.depositapp.db.SendDepositResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// 2. ViewModel 구현
class ResultViewModel(private val repository: NotificationRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<List<SendDepositResult>>(emptyList())
    val uiState: StateFlow<List<SendDepositResult>> = _uiState.asStateFlow()

    init {
        refreshState()
    }

    fun refreshState() {
        viewModelScope.launch {
            _uiState.value = repository.getAllSendDepositResult()
        }
    }

    fun onDeleteSucceededButtonClick() {
        viewModelScope.launch {
            repository.deleteSucceededResult()
        }
        refreshState()
    }

    fun onDeleteFailedButtonClick() {
        viewModelScope.launch {
            repository.deleteFailedResult()
        }
        refreshState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as MyApplication).repository
                ResultViewModel(repository)
            }
        }
    }
}
