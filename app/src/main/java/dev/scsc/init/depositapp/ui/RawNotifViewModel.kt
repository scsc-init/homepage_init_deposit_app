package dev.scsc.init.depositapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.scsc.init.depositapp.MyApplication
import dev.scsc.init.depositapp.db.NotificationRepository
import dev.scsc.init.depositapp.db.RawNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// 2. ViewModel 구현
class RawNotifViewModel(private val repository: NotificationRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(listOf<RawNotification>())
    val uiState: StateFlow<List<RawNotification>> = _uiState.asStateFlow()

    fun refreshState() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = repository.getAllRawNotif()
        }
    }
    
    fun onParseButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.processAndStoreNotifications()
            refreshState()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as MyApplication).repository
                RawNotifViewModel(repository)
            }
        }
    }
}
