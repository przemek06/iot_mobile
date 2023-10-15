package edu.pwr.iotmobile.androidimcs.ui.screens.search

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val mockUser = User(
    id = "1",
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.Normal
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(users = listOf(mockUser, mockUser, mockUser, mockUser))
        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(searchInputFieldData = text)
        }
    }
    fun addAdmin(user: User) {
        _uiState.update { it.copy() }
    }
    fun blockUser(user: User) {
        _uiState.update { it.copy() }
    }
    fun unblockUser(user: User) {
        _uiState.update { it.copy() }
    }
}