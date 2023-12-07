package edu.pwr.iotmobile.androidimcs.ui.screens.account

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.User.Companion.toUser
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import edu.pwr.iotmobile.androidimcs.service.ServiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userRepository: UserRepository,
    val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: AccountNavigation) {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }

        val changePasswordOption = MenuOption(
            titleId = R.string.change_password
        ) { navigation.openChangePassword() }

        viewModelScope.launch {
            try {
                val result = userRepository.getActiveUserInfo()

                if (result.isSuccess) {
                    val user = result.getOrNull()?.toUser() ?: return@launch

                    _uiState.update {
                        it.copy(
                            user = user,
                            changePasswordOption = changePasswordOption,
                            isError = false,
                            isLoading = false
                        )
                    }
                } else {
                    Log.d("Account", "Error while getting user data")
                    _uiState.update {
                        it.copy(
                            isError = true,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Account", "Error while getting user data", e)
                _uiState.update {
                    it.copy(
                        isError = true,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setDisplayName(displayName: String) {
        if (displayName.isBlank()) {
            _uiState.update {
                it.copy(inputField = it.inputField.copy(isError = true))
            }
            return
        }
        updateLoading(true)

        viewModelScope.launch {
            try {
                val result = userRepository.updateActiveUser(
                    UserDto(
                        email = uiState.value.user.email,
                        password = "12345678",
                        name = displayName
                    )
                )

                result.onSuccess {
                    val user = userRepository
                        .getActiveUserInfo()
                        .getOrNull()
                        ?.toUser() ?: return@launch

                    _uiState.update {
                        it.copy(
                            user = user,
                            inputField = it.inputField.copy(isError = false),
                            isLoading = false
                        )
                    }

                    toast.toast("Successfully updated display name")
                }.onFailure {
                    updateLoading(false)
                    toast.toast("Could not update display name.")
                }
            } catch (e: Exception) {
                Log.e("Account", "Error when updating user data.", e)
                updateLoading(false)
                toast.toast("Could not update display name.")
            }
        }
    }

    fun deleteAccount(navigation: AccountNavigation) {
        updateLoading(true)
        viewModelScope.launch {
            try {
                val result = userRepository.deleteActiveUser()

                result.onSuccess {
                    toast.toast("Successfully deleted account")
                    navigation.openLogin()
                    return@launch
                }
                updateLoading(false)
                toast.toast("Failed to delete user account.")
            } catch (e: Exception) {
                Log.e("Account", "Error while logging out.", e)
                updateLoading(false)
                toast.toast("Failed to delete user account.")
            }
        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputField = it.inputField.copy(text = text))
        }
    }

    fun logout(
        navigation: AccountNavigation,
        context: Context
    ) {
        updateLoading(true)
        viewModelScope.launch {
            try {
                val result = userRepository.logout()
                result.onSuccess {
                    toast.toast("Successfully logged out")
                    navigation.openLogin()
                }
                result.onFailure {
                    toast.toast("Could not log out")
                }
                updateLoading(false)
            } catch (e: Exception) {
                Log.e("Account", "Error while logging out.", e)
                updateLoading(false)
            }
        }
        ServiceManager.serviceStop(context)
    }

    private fun updateLoading(value: Boolean) {
        _uiState.update {
            it.copy(isLoading = value)
        }
    }
}