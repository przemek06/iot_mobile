package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import android.content.Context

interface AdminUiInteraction {
    fun logout(context: Context)
    fun deleteAccount()

    companion object {
        fun default(
            viewModel: AdminViewModel,
            navigation: AdminNavigation
        ) = object : AdminUiInteraction {
            override fun logout(context: Context) {
                viewModel.logout(navigation, context)
            }

            override fun deleteAccount() {
                viewModel.deleteAccount(navigation)
            }

        }
    }
}