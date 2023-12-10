package edu.pwr.iotmobile.androidimcs.ui.screens.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    val toast: Toast,
    val event: Event
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                userRepository.getLoggedInUser().collect { user ->
                    _uiState.update {
                        it.copy(
                            isUserLoggedIn = user != null,
                            isUserAdmin = user?.role == UserRole.ADMIN_ROLE
                        )
                    }

                    if (_uiState.value.isLoading) {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }

                val invitations = projectRepository.findAllPendingInvitationsForActiveUser()
                val isInvitation = invitations.isNotEmpty()
                _uiState.update { it.copy(isInvitation = isInvitation) }
            }
        }
    }
}