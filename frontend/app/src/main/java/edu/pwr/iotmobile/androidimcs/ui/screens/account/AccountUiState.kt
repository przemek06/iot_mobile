package edu.pwr.iotmobile.androidimcs.ui.screens.account

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.StatData
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole

data class AccountUiState(
    val user: User,
    val changePasswordOption: MenuOption,
    val inputField: InputFieldData,
    val statList: List<StatData>,
    val options: List<MenuOption>,
    val isLoading: Boolean,
    val isError: Boolean
) {
    companion object {
        fun default(
            displayName: String = "display name",
            email: String = "email",
            changePasswordOption: MenuOption = MenuOption(titleId = R.string.change_password),
            inputField: InputFieldData = InputFieldData(
                text = "",
                label = R.string.name,
                errorMessage = R.string.not_empty,
                isError = false
            ),
            statList: List<StatData> = emptyList(),
            options: List<MenuOption> = emptyList(),
            user: User = User(
                id = 0,
                displayName = "",
                email = "",
                role = UserRole.USER_ROLE
            )
        ) = AccountUiState(
            changePasswordOption = changePasswordOption,
            inputField = inputField,
            statList = statList,
            options = options,
            user = user,
            isLoading = true,
            isError = false
        )
    }
}