package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

interface RegisterUiInteraction {
    fun onTextChange(type: RegisterViewModel.InputFieldType, text: String)
    fun onRegister()

    companion object {
        fun default(
            viewModel: RegisterViewModel
        ) = object : RegisterUiInteraction {
            override fun onTextChange(type: RegisterViewModel.InputFieldType, text: String) {
                viewModel.onTextChange(type, text)
            }

            override fun onRegister() {
                TODO("Not yet implemented")
            }

        }
    }
}