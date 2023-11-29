package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val userRepository: UserRepository,
    private val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: AdminNavigation) {

        var adminOptions = listOf(
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

    fun deleteAccount(navigation: AdminNavigation) {
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository.deleteActiveUser()
            }.onSuccess {
                toast.toast("Deleted account")
                navigation.openLogin()
                return@launch
            }.onFailure {
                toast.toast("Failed deleting account")
            }
        }
    }

    fun logout(navigation: AdminNavigation) {
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository.logout()
            }.onSuccess {
                toast.toast("Logged out")
                navigation.openLogin()
            }.onFailure {
                toast.toast("Couldn't log out")
            }
        }
    }
}