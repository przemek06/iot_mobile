package edu.pwr.iotmobile.androidimcs.ui.screens.account

interface AccountUiInteraction {

    fun setDisplayName(displayName: String)
    fun onTextChange(text: String)

    companion object {
        fun default(
            viewModel: AccountViewModel
        ) = object : AccountUiInteraction {
            override fun setDisplayName(displayName: String) {
                viewModel.setDisplayName(displayName)
            }

            override fun onTextChange(text: String) {
                viewModel.onTextChange(text = text)
            }
        }
    }
}