package edu.pwr.iotmobile.androidimcs.ui.screens.account

interface AccountUiInteraction {

    fun setDisplayName(displayName: String)
    fun deleteAccount(navigation: AccountNavigation)
    fun onTextChange(text: String)
    fun logout(navigation: AccountNavigation)

    companion object {
        fun default(
            viewModel: AccountViewModel
        ) = object : AccountUiInteraction {
            override fun setDisplayName(displayName: String) {
                viewModel.setDisplayName(displayName)
            }
            override fun deleteAccount(navigation: AccountNavigation) {
                viewModel.deleteAccount(navigation)
            }
            override fun onTextChange(text: String) {
                viewModel.onTextChange(text = text)
            }

            override fun logout(navigation: AccountNavigation) {
                viewModel.logout(navigation)
            }
        }
    }
}