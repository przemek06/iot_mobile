package edu.pwr.iotmobile.androidimcs.ui.screens.search

import edu.pwr.iotmobile.androidimcs.data.User

interface SearchUiInteraction {

    fun onTextChange(text: String)
    fun setSelectedUser(user: User)
    fun setDialogVisible()
    fun setDialogInvisible()
    fun addAdmin(user: User)
    fun blockUser(user: User)
    fun unblockUser(user: User)

    companion object {
        fun default(viewModel: SearchViewModel) = object : SearchUiInteraction {

            override fun onTextChange(text: String) {
                viewModel.onTextChange(text)
            }

            override fun setSelectedUser(user: User) {
                viewModel.setSelectedUser(user)
            }

            override fun setDialogVisible() {
                viewModel.setDialogVisible()
            }
            override fun setDialogInvisible() {
                viewModel.setDialogInvisible()
            }

            override fun addAdmin(user: User) {
                viewModel.addAdmin(user)
            }
            override fun blockUser(user: User) {
                viewModel.blockUser(user)
            }
            override fun unblockUser(user: User) {
                viewModel.unblockUser(user)
            }
        }
    }
}