package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import edu.pwr.iotmobile.androidimcs.data.InputFieldData

interface RegisterUiInteraction {
    fun onTextChange(item: InputFieldData, text: String)
    fun onRegister()

    companion object {
        fun default(
            viewModel: RegisterViewModel
        ) = object : RegisterUiInteraction {
            override fun onTextChange(item: InputFieldData, text: String) {
                viewModel.onTextChange(item, text)
            }

            override fun onRegister() {
                TODO("Not yet implemented")
            }

        }
    }
}