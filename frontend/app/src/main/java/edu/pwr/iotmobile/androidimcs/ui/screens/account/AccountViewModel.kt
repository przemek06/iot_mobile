package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccountViewModel : ViewModel() {

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

    fun onTextChangePassword(text: String) {
        _uiState.update {
            it.copy(inputFieldPassword = it.inputFieldPassword.copy(text = text))
        }
    }
    fun onTextChangePasswordNew(text: String) {
        _uiState.update {
            it.copy(inputFieldPasswordNew = it.inputFieldPasswordNew.copy(text = text))
        }
    }
}