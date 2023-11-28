package edu.pwr.iotmobile.androidimcs.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                val invitations = projectRepository.findAllPendingInvitationsForActiveUser()
                val isInvitation = invitations.isNotEmpty()

                val user = userRepository.getLoggedInUser().firstOrNull() ?: run {
                    _uiState.update { it.copy(
                        isError = true,
                        isLoading = false
                    ) }
                    return@launch
                }
                _uiState.update { it.copy(
                    isInvitation = isInvitation,
                    user = user,
                    isLoading = false,
                    isError = false
                ) }
            }
        }
    }
}