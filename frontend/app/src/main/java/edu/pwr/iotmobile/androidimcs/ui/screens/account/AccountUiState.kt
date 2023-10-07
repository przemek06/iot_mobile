package edu.pwr.iotmobile.androidimcs.ui.screens.account

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption

data class AccountUiState(
    val displayName: String,
    val email: String,
    val changePasswordOption: MenuOption,
    val inputField: InputFieldData,
    val dashboardsTotal: Int,
    val accessedDashboards: Int,
    val createdTopicGroups: Int,
    val inputFieldPassword: InputFieldData,
    val inputFieldPasswordNew: InputFieldData
) {
    companion object {
        fun default(
            displayName: String = "display name",
            email: String = "email",
            changePasswordOption: MenuOption = MenuOption(titleId = R.string.change_password),
            inputField: InputFieldData = InputFieldData(label = R.string.display_name),
            inputFieldPassword: InputFieldData = InputFieldData(label = R.string.password),
            inputFieldPasswordNew: InputFieldData = InputFieldData(label = R.string.new_password),
            dashboardsTotal: Int = 0,
            accessedDashboards: Int = 0,
            createdTopicGroups: Int = 0
        ) = AccountUiState(
            displayName = displayName,
            email = email,
            changePasswordOption = changePasswordOption,
            inputField = inputField,
            inputFieldPassword = inputFieldPassword,
            inputFieldPasswordNew = inputFieldPasswordNew,
            dashboardsTotal = dashboardsTotal,
            accessedDashboards = accessedDashboards,
            createdTopicGroups = createdTopicGroups
        )
    }
}