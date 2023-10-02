package edu.pwr.iotmobile.androidimcs.ui.screens.account

data class AccountUiState(
    val displayName: String
) {
    companion object {
        fun default(
            displayName: String = ""
        ) = AccountUiState(
            displayName = displayName
        )
    }
}