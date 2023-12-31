package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

interface InvitationsUiInteraction {

    fun acceptInvitation(id: Int)
    fun declineInvitation(id: Int)

    companion object {
        fun default(viewModel: InvitationsViewModel) = object : InvitationsUiInteraction{
            override fun acceptInvitation(id: Int) {
                viewModel.acceptInvitation(id)
            }
            override fun declineInvitation(id: Int) {
                viewModel.declineInvitation(id)
            }

        }
    }
}