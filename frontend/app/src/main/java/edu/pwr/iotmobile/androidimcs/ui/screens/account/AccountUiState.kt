package edu.pwr.iotmobile.androidimcs.ui.screens.account

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginViewModel

data class AccountUiState(
    val displayName: String,
    val username: String,
    val userOptionList: List<MenuOption>,
    val inputField: InputFieldData,
    val dashboardsTotal: Int,
    val accessedDashboards: Int,
    val createdTopicGroups: Int,
) {
    companion object {
        fun default(
            displayName: String = "display name",
            username: String = "username",
            userOptionList: List<MenuOption> = listOf(),
            inputField: InputFieldData = InputFieldData(
                text = "",
                label = R.string.nothing
            ),
            dashboardsTotal: Int = 0,
            accessedDashboards: Int = 0,
            createdTopicGroups: Int = 0
        ) = AccountUiState(
            displayName = displayName,
            username = username,
            userOptionList = userOptionList,
            inputField = inputField,
            dashboardsTotal = dashboardsTotal,
            accessedDashboards = accessedDashboards,
            createdTopicGroups = createdTopicGroups
        )
    }
}