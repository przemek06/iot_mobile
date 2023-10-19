package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

interface RegisterUiInteraction {
    fun onTextChange(type: RegisterViewModel.InputFieldType, text: String)
    fun onRegister()
    fun checkData()

    companion object {
        fun default(
            viewModel: RegisterViewModel
        ) = object : RegisterUiInteraction {
            override fun onTextChange(type: RegisterViewModel.InputFieldType, text: String) {
                viewModel.onTextChange(type, text)
            }

            override fun onRegister() {
                viewModel.onRegisterClick()
            }

            override fun checkData() {
                viewModel.checkData()
            }

        }
    }
}