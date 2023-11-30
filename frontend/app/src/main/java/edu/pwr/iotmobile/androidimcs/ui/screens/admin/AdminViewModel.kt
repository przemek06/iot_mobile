package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
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

        val adminOptions = listOf(
            MenuOption(
                titleId = R.string.admin_3,
                onClick = { navigation.openBanUsers() }
            ),
            MenuOption(
                titleId = R.string.admin_5,
                onClick = { navigation.openAddAdmin() }
            )
        )

        val changePasswordOption = MenuOption(
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
                if (it.isSuccess) {
                    toast.toast("Successfully deleted account.")
                    navigation.openLogin()
                } else {
                    toast.toast("Failed deleting account.")
                }
            }.onFailure {
                toast.toast("Failed deleting account.")
            }
        }
    }

    fun logout(navigation: AdminNavigation) {
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository.logout()
            }.onSuccess {
                if (it.isSuccess) {
                    toast.toast("Successfully logged out.")
                    navigation.openLogin()
                } else {
                    toast.toast("Could not log out.")
                }
            }.onFailure {
                toast.toast("Could not log out.")
            }
        }
    }
}