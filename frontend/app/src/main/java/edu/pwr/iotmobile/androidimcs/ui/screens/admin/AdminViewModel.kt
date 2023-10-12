package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: AdminNavigation) {

        var adminOptions = listOf<MenuOption>(
            MenuOption(
                titleId = R.string.admin_3,
                onClick = { navigation.openBanUsers() }
            ),
            MenuOption(
                titleId = R.string.admin_4,
                onClick = { navigation.openBannedUsers() }
            ),
            MenuOption(
                titleId = R.string.admin_5,
                onClick = { navigation.openAddAdmin() }
            )
        )

        var changePasswordOption = MenuOption(
            titleId = R.string.change_password,
            onClick = { navigation.openChangePassword() }
        )

        _uiState.update {
            it.copy(
                adminOptions = adminOptions,
                changePasswordOption = changePasswordOption
            )
        }
    }
}