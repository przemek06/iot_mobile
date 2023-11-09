package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

import edu.pwr.iotmobile.androidimcs.data.InvitationData

data class InvitationsUiState(
    val invitations: List<InvitationData>
) {
    companion object {
        fun default(
            invitations: List<InvitationData> = emptyList()
        ) = InvitationsUiState (
            invitations = invitations
        )
    }
}
