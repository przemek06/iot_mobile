package edu.pwr.iotmobile.androidimcs.ui.screens.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    val toast: Toast,
    val event: Event
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Log.d("User", "init called")
        viewModelScope.launch(Dispatchers.Default) {
            userRepository.getLoggedInUser().collect { user ->
                _uiState.update {
                    it.copy(isUserLoggedIn = user != null)
                }
                Log.d("User", user.toString())

                if (_uiState.value.isLoading) {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    companion object {
        const val USER_LOGGED_IN = "userLoggedIn"
        const val USER_LOGGED_OUT = "userLoggedOut"
    }
}