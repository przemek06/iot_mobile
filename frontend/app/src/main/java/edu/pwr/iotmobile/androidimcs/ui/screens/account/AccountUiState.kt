package edu.pwr.iotmobile.androidimcs.ui.screens.account

import edu.pwr.iotmobile.androidimcs.data.MenuOption

data class AccountUiState(
    val displayName: String,
    val username: String,
    val userOptionList: List<MenuOption>
) {
    companion object {
        fun default(
            displayName: String = "",
            username: String,
            userOptionList: List<MenuOption>
        ) = AccountUiState(
            displayName = displayName,
            username = username,
            userOptionList = userOptionList
        )
    }
}