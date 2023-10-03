package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword

interface ForgotPasswordUiInteraction {
    fun onTextChange(type: ForgotPasswordViewModel.InputFieldType, text: String)
    fun onConfirmEmail()
    fun onConfirmNewPassword()
    fun onResendCode()

    companion object {
        fun default(
            viewModel: ForgotPasswordViewModel
        ) = object : ForgotPasswordUiInteraction {
            override fun onTextChange(type: ForgotPasswordViewModel.InputFieldType, text: String) {
                viewModel.onTextChange(type, text)
            }

            override fun onConfirmEmail() {
                viewModel.onConfirmEmail()
            }

            override fun onConfirmNewPassword() {
                TODO("Not yet implemented")
            }

            override fun onResendCode() {
                viewModel.onResendCode()
            }

        }
    }
}