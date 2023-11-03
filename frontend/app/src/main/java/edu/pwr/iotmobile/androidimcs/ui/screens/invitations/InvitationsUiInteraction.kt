package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

interface InvitationsUiInteraction {

    fun getInvitations()
    fun acceptInvitation()
    fun declineInvitation()

    companion object {
        fun default(viewModel: InvitationsViewModel) = object : InvitationsUiInteraction{
            override fun getInvitations() {
                viewModel.getInvitations()
            }
            override fun acceptInvitation() {
                viewModel.acceptInvitation()
            }
            override fun declineInvitation() {
                viewModel.declineInvitation()
            }

        }
    }
}