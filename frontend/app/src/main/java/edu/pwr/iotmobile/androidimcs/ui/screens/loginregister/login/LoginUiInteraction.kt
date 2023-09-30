package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

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
                TODO("Not yet implemented")
            }

        }
    }
}