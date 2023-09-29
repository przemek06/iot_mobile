package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

interface LoginUiInteraction {
    fun onTextChange(item: InputFieldData, text: String)
    fun onLogin()

    companion object {
        fun default(
            viewModel: LoginViewModel
        ) = object : LoginUiInteraction {
            override fun onTextChange(item: InputFieldData, text: String) {
                viewModel.onTextChange(item, text)
            }

            override fun onLogin() {
                TODO("Not yet implemented")
            }

        }
    }
}