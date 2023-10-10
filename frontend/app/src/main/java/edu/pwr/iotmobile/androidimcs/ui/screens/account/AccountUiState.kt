package edu.pwr.iotmobile.androidimcs.ui.screens.account

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.StatData

data class AccountUiState(
    val displayName: String,
    val email: String,
    val changePasswordOption: MenuOption,
    val inputField: InputFieldData,
    val statList: List<StatData>,
    val options: List<MenuOption>
) {
    companion object {
        fun default(
            displayName: String = "display name",
            email: String = "email",
            changePasswordOption: MenuOption = MenuOption(titleId = R.string.change_password),
            inputField: InputFieldData = InputFieldData(label = R.string.display_name),
            statList: List<StatData> = listOf(
                StatData(label = R.string.dashboards_total),
                StatData(label = R.string.accessed_dashboards),
                StatData(label = R.string.created_topics_groups)
            ),
            options: List<MenuOption> = emptyList()
        ) = AccountUiState(
            displayName = displayName,
            email = email,
            changePasswordOption = changePasswordOption,
            inputField = inputField,
            statList = statList,
            options = options
        )
    }
}