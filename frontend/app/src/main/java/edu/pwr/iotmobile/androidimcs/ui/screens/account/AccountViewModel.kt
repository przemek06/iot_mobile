package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.StatData
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.User.Companion.toUser
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userRepository: UserRepository,
    private val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: AccountNavigation) {

        val changePasswordOption = MenuOption(
            titleId = R.string.change_password
        ) { navigation.openChangePassword() }

        viewModelScope.launch {
            val result = userRepository.getActiveUserInfo()
            val user = result.getOrNull()?.toUser() ?: return@launch

            _uiState.update {
                it.copy(user = user)
            }
        }

        _uiState.update {
            it.copy(
                changePasswordOption = changePasswordOption
            )
        }
    }
    fun setDisplayName(displayName: String) {

        if(displayName.isNotBlank()){
            viewModelScope.launch {

                val result = userRepository.updateActiveUser(UserDto(
                    email = uiState.value.user.email,
                    password = "12345678", // TODO: xD?
                    name = displayName
                ))

                result.onSuccess {

                    val user = userRepository
                        .getActiveUserInfo()
                        .getOrNull()
                        ?.toUser() ?: return@launch

                    _uiState.update {
                        it.copy(user = user)
                    }

                    toast.toast("Updated display name")
                }
            }
        } else {
            _uiState.update {
                it.copy(inputField = it.inputField.copy(isError = true))
            }
        }
    }

    fun deleteAccount(navigation: AccountNavigation) {
        viewModelScope.launch {
            val result = userRepository.deleteActiveUser()

            result.onSuccess {
                toast.toast("Deleted account")
                navigation.openLogin()
                return@launch
            }
            toast.toast("Failed deleting account")

        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputField = it.inputField.copy(text = text))
        }
    }

    fun logout(navigation: AccountNavigation) {
        viewModelScope.launch {
            val result = userRepository.logout()
            result.onSuccess {
                toast.toast("Logged out")
                navigation.openLogin()
            }
            result.onFailure {
                toast.toast("Couldn't log out")
            }
        }
    }
}