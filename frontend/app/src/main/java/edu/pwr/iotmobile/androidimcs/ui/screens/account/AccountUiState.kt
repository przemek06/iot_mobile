package edu.pwr.iotmobile.androidimcs.ui.screens.account

import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginViewModel

data class AccountUiState(
    val displayName: String,
    val username: String,
    val userOptionList: List<MenuOption>,
    val inputField: InputFieldData
) {
    companion object {
        fun default(
            displayName: String = "",
            username: String = "",
            userOptionList: List<MenuOption> = emptyList(),
            inputField: InputFieldData = InputFieldData(
                text = "",
                label = 0
            )
        ) = AccountUiState(
            displayName = displayName,
            username = username,
            userOptionList = userOptionList,
            inputField = inputField
        )
    }
}