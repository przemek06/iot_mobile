package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                displayName = "DisplayName"
            )
        }
    }

    fun setDisplayName(displayName: String) {
        _uiState.update {
            it.copy(displayName = displayName)
        }
    }
}