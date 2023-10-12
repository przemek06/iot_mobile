package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

interface ActivateAccountUiInteraction {
    fun onTextChange(text: String)
    fun onActivate()
    fun onResendCode()

    companion object {
        fun default(
            viewModel: ActivateAccountViewModel
        ) = object : ActivateAccountUiInteraction {
            override fun onTextChange(text: String) {
                viewModel.onTextChange(text)
            }

            override fun onActivate() {
                viewModel.onActivate()
            }

            override fun onResendCode() {
                viewModel.onResendCode()
            }

        }
    }
}