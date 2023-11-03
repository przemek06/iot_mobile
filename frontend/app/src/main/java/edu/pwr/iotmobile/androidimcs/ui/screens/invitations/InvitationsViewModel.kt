package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.data.InvitationData
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val mockUser = User(
    id = 1,
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.USER_ROLE,
    isBlocked = true
)

class InvitationsViewModel(
    private val userRepository: UserRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvitationsUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init() {
        _uiState.update {
            it.copy(invitations = listOf(
                InvitationData(
                    user = mockUser,
                    projectName = "project1"
                ),
                InvitationData(
                    user = mockUser,
                    projectName = "project2"
                )
            ))
        }
    }

    fun getInvitations() {
        // TODO: need repo for it
    }
    fun acceptInvitation() {
        // TODO: need repo for it
    }
    fun declineInvitation() {
        // TODO: need repo for it
    }
}