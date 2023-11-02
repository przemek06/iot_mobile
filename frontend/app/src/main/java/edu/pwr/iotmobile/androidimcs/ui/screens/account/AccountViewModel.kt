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
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: AccountNavigation) {

        val changePasswordOption = MenuOption(
            titleId = R.string.change_password
        ) { navigation.openChangePassword() }

        val stats = generateStats(0, 0, 0)

        viewModelScope.launch {
            val result = userRepository.getActiveUserInfo()
            val userInfoDto = result.getOrNull() ?: return@launch
            val user = userInfoDto.toUser()

            _uiState.update {
                it.copy(user = user)
            }
        }

        _uiState.update {
            it.copy(
                changePasswordOption = changePasswordOption,
                statList = stats
            )
        }
    }
    fun setDisplayName(displayName: String) {

        /*
        * checkData()
        if (_uiState.value.inputFields.any { it.value.isError }) return

        viewModelScope.launch {
            val userDto = _uiState.value.inputFields.toDto() ?: return@launch
            val result = userRepository.register(userDto)
            Log.d("register", "result: ${result.name}")
            when (result) {
                RegisterUserResult.Success -> event.event(REGISTER_SUCCESS_EVENT)
                RegisterUserResult.AccountExists -> toast.toast("Account with this email already exists.")
                RegisterUserResult.Failure -> toast.toast("Error - could not register.")
            }
        }*/

        if(displayName.isNotBlank()){
            viewModelScope.launch {

                val userDtoInfo = userRepository.getActiveUserInfo().getOrElse { return@launch }

                val result = userRepository.updateActiveUser(UserDto(
                    email = userDtoInfo.email,
                    password = "12345678", // TODO: xD? 
                    name = userDtoInfo.name
                ))

//                if(result.isSuccess){
//                    _uiState.update {
//                        it.copy(user = userDtoInfo.toUser())
//                    }
//                }
            }
        } else {
            _uiState.update {
                it.copy(inputField = it.inputField.copy(isError = true))
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            userRepository.deleteActiveUser()
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = userRepository.logout()
        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputField = it.inputField.copy(text = text))
        }
    }

    fun generateStats(
        dashboardsTotal: Int,
        accessedDashboards: Int,
        createdTopicsGroups: Int
    ): List<StatData> {
        return listOf(
            StatData(
                label = R.string.dashboards_total,
                value = dashboardsTotal.toString()
            ),
            StatData(
                label = R.string.accessed_dashboards,
                value = accessedDashboards.toString()
            ),
            StatData(
                label = R.string.created_topics_groups,
                value = createdTopicsGroups.toString()
            )
        )
    }


}