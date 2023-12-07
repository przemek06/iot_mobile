package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.data.InvitationData
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class InvitationsViewModel(
    private val projectRepository: ProjectRepository,
    val event: Event,
    val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvitationsUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        getInvitations()
    }

    fun acceptInvitation(id: Int) {
        viewModelScope.launch {
            try {
                val result = projectRepository.acceptInvitation(id)

                if (result.isSuccess) {
                    val invitations = projectRepository.findAllPendingInvitationsForActiveUser()
                    _uiState.update {
                        it.copy(invitations = invitations.map { dto ->
                            InvitationData(
                                id = dto.id,
                                projectName = dto.project.name
                            )
                        })
                    }
                    toast.toast("Invitation successfully accepted!")
                    return@launch
                }
                toast.toast("Failed to accept invitation.")
            } catch (e: Exception) {
                Log.e("Invitations", "Error while accepting invitation.", e)
                toast.toast("Failed to accept invitation.")
            }
        }
    }

    fun declineInvitation(id: Int) {
        viewModelScope.launch {
            try {
                val result = projectRepository.rejectInvitation(id)

                if(result.isSuccess) {
                    val invitations = projectRepository.findAllPendingInvitationsForActiveUser()
                    _uiState.update {
                        it.copy(invitations = invitations.map { dto ->
                            InvitationData(
                                id = dto.id,
                                projectName = dto.project.name
                            )
                        })
                    }
                    toast.toast("Invitation successfully declined.")
                    return@launch
                }
                toast.toast("Failed to accept invitation.")
            } catch (e: Exception) {
                Log.e("Invitations", "Error while accepting invitation.", e)
                toast.toast("Failed to accept invitation.")
            }
        }
    }

    private fun getInvitations() {
        viewModelScope.launch {
            val invitationDtos = projectRepository.findAllPendingInvitationsForActiveUser()

            val invitations = invitationDtos.map { InvitationData(
                id = it.id,
                projectName = it.project.name
            ) }

            _uiState.update {
                it.copy(invitations = invitations)
            }
        }
    }
}