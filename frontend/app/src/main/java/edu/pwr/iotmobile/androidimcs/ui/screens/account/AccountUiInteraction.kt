package edu.pwr.iotmobile.androidimcs.ui.screens.account

interface AccountUiInteraction {

    fun setDisplayName(displayName: String)
    fun onTextChange(text: String)
    fun onTextChangePassword(text: String)
    fun onTextChangePasswordNew(text: String)

    companion object {
        fun default(
            viewModel: AccountViewModel
        ) = object : AccountUiInteraction {
            override fun setDisplayName(displayName: String) {
                viewModel.setDisplayName(displayName)
            }

            override fun onTextChange(text: String) {
                viewModel.onTextChange(text = text)
            }
            override fun onTextChangePassword(text: String) {
                viewModel.onTextChangePassword(text = text)
            }
            override fun onTextChangePasswordNew(text: String) {
                viewModel.onTextChangePasswordNew(text = text)
            }
        }
    }
}