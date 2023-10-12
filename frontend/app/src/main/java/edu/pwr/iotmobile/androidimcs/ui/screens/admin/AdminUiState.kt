package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption

data class AdminUiState(
    val adminOptions: List<MenuOption>,
    val changePasswordOption: MenuOption
) {
    companion object {
        fun default (
            adminOptions: List<MenuOption> = emptyList(),
            changePasswordOption: MenuOption = MenuOption(titleId = R.string.change_password),
        ) = AdminUiState (
            adminOptions = adminOptions,
            changePasswordOption = changePasswordOption
        )
    }
}