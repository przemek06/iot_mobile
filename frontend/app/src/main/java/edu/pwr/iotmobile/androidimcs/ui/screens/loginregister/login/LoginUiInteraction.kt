package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

interface LoginUiInteraction {
    fun onTextChange(item: InputFieldData, text: String)

    companion object {
        fun default(
            viewModel: LoginViewModel
        ) = object : LoginUiInteraction {
            override fun onTextChange(item: InputFieldData, text: String) {
                viewModel.onTextChange(item, text)
            }

        }
    }
}