package edu.pwr.iotmobile.androidimcs.ui.screens.account

import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsUiInteraction
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsViewModel

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