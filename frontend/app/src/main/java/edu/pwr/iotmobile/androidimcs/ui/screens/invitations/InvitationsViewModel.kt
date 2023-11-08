package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.data.InvitationData
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val mockUser = User(
    id = 1,
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.USER_ROLE,
    isBlocked = true
)

class InvitationsViewModel(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvitationsUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init() {
        getInvitations()
    }

    fun getInvitations() {
        viewModelScope.launch {
            val invitationDtos = projectRepository.findAllPendingInvitationsForActiveUser()

            val deferredInvitations = invitationDtos.map { dto ->
                async {
                    val project = projectRepository.getProjectById(dto.projectId)
                    val user = userRepository.getUserInfoById(dto.userId).getOrNull()

                    if (project != null && user != null) {
                        InvitationData(
                            id = dto.id,
                            projectName = project.name,
                            userName = user.name
                        )
                    } else {
                        null
                    }
                }
            }

            val invitations = deferredInvitations.awaitAll().filterNotNull()

            _uiState.update {
                it.copy(invitations = invitations)
            }
        }
    }
    fun acceptInvitation(id: Int) {
        viewModelScope.launch {
            val result = projectRepository.acceptInvitation(id)

            if(result.isSuccess) {
                _uiState.update {
                    it.copy(invitations = it.invitations.filter {
                            invitation -> invitation.id != id
                    })
                }
            }
        }
    }
    fun declineInvitation(id: Int) {
        // TODO:
    }
}