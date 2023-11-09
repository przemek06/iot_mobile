package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.StatData
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

    init {
        _uiState.update {
            it.copy(
                displayName = "DisplayName",
                inputField = InputFieldData(
                    text = "",
                    label = R.string.name,
                    errorMessage = R.string.not_empty,
                    isError = false
                )
            )
        }
    }

    fun init(navigation: AccountNavigation) {

        var changePasswordOption = MenuOption(
            titleId = R.string.change_password,
            onClick = { navigation.openChangePassword() }
        )

        val options = listOf(
            MenuOption(
                titleId = R.string.change_password,
                onClick = { navigation.openChangePassword() }
            ),

        )

        _uiState.update {
            it.copy(changePasswordOption = changePasswordOption)
        }
    }

    fun setDisplayName(displayName: String) {
        _uiState.update {
            it.copy(displayName = displayName)
        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputField = it.inputField.copy(text = text))
        }
    }

    fun logout(navigation: AccountNavigation) {
        viewModelScope.launch {
            val result = userRepository.logOut()
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