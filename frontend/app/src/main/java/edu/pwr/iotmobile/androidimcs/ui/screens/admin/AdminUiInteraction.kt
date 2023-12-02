package edu.pwr.iotmobile.androidimcs.ui.screens.admin

interface AdminUiInteraction {
    fun logout()
    fun deleteAccount()

    companion object {
        fun default(
            viewModel: AdminViewModel,
            navigation: AdminNavigation
        ) = object : AdminUiInteraction {
            override fun logout() {
                viewModel.logout(navigation)
            }

            override fun deleteAccount() {
                viewModel.deleteAccount(navigation)
            }

        }
    }
}