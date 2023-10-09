package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

interface LoginUiInteraction {
    fun onTextChange(type: LoginViewModel.InputFieldType, text: String)
    fun onLogin()

    companion object {
        fun default(
            viewModel: LoginViewModel
        ) = object : LoginUiInteraction {
            override fun onTextChange(type: LoginViewModel.InputFieldType, text: String) {
                viewModel.onTextChange(type, text)
            }

            override fun onLogin() {
                viewModel.onLoginClick()
            }

        }
    }
}