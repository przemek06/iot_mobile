package edu.pwr.iotmobile.androidimcs.ui.screens.changepassword


interface ChangePasswordUiInteraction {

    fun onTextChangePassword(text: String)
    fun onTextChangePasswordNew(text: String)

    companion object {
        fun default(
            viewModel: ChangePasswordViewModel
        ) = object : ChangePasswordUiInteraction {
            override fun onTextChangePassword(text: String) {
                viewModel.onTextChangePassword(text = text)
            }
            override fun onTextChangePasswordNew(text: String) {
                viewModel.onTextChangePasswordNew(text = text)
            }
        }
    }
}