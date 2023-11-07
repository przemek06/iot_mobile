package edu.pwr.iotmobile.androidimcs.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.User.Companion.toUser
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDtoSend
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val mockUser = User(
    id = 1,
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.USER_ROLE,
    isBlocked = true
)

class SearchViewModel(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    private val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        val users = listOf(mockUser, mockUser, mockUser, mockUser)
        _uiState.update {
            it.copy(
                users = users,
                searchedUsers = users
            )
        }
    }

    fun init(
        mode: SearchMode,
        navigation: SearchNavigation
    ) {
        val data = when(navigation.mode) {
            SearchMode.ADD_ADMIN -> ScreenData(
                topBarText = R.string.add_admin,
                buttonText = R.string.add_admin,
                dialogTitle = R.string.search_admin_1,
                dialogButton2Function = {
                    addAdmin(it)
                    setDialogInvisible()
                }
            )
            SearchMode.BLOCK_USERS -> ScreenData(
                topBarText = R.string.block_users,
                buttonText = R.string.block,
                buttonTextAlternative = R.string.unblock,
                dialogTitle = R.string.search_block_1,
                dialogTitleAlternative = R.string.search_block_2,
                dialogButton2Function = {
                    toggleBlockUser(it)
                    setDialogInvisible()
                },
                dialogButton2FunctionAlternative = {
                    toggleBlockUser(it)
                    setDialogInvisible()
                },
                alternative = {
                    it.isBlocked
                }
            )
            SearchMode.INVITE_USERS -> ScreenData(
                topBarText = R.string.invite_users,
                buttonText = R.string.invite,
                dialogTitle = R.string.sure_invite,
                dialogButton2Function = {
                    inviteUser(
                        userId = it.id,
                        projectId = navigation.projectId
                    )
                },
                getUsersFunction = { getAllUsers() }
            )
            SearchMode.NONE -> ScreenData(
                topBarText = R.string.nothing,
                buttonText = R.string.nothing
            )
        }
        _uiState.update {
            it.copy(data = data)
        }
    }

    fun onTextChange(text: String) {

        val searchedUsers = uiState.value.users.filter {
            it.displayName.contains(other = text, ignoreCase = true)
        }

        _uiState.update {
            it.copy(
                searchInputFieldData = text,
                searchedUsers = searchedUsers
            )
        }
    }
    fun setSelectedUser(user: User) {
        _uiState.update {
            it.copy(selectedUser = user)
        }
    }
    fun setDialogVisible() {
        _uiState.update {
            it.copy(isDialogVisible = true)
        }
    }
    fun setDialogInvisible() {
        _uiState.update {
            it.copy(isDialogVisible = false)
        }
    }
    fun addAdmin(user: User) {

    }
    fun toggleBlockUser(user: User) {

    }
    private fun inviteUser(userId: Int, projectId: Int?) {
        viewModelScope.launch {
            projectId?.let { projectId ->
                val result = projectRepository.createInvitation(InvitationDtoSend(
                    projectId = projectId,
                    userId = userId
                ))
                if(result.isSuccess) {
                    toast.toast("Invited user")
                    return@launch
                }
            }
            toast.toast("Failure")
        }
    }

    private fun getAllUsers() {
        viewModelScope.launch {
            val result = userRepository.getAllUserInfo()
            result.onSuccess { list ->
                val users = list.mapNotNull { it.toUser() }
                _uiState.update { state ->
                    state.copy(
                        users = users,
                        searchedUsers = users
                    )
                }
            }
            result.onFailure { toast.toast("Cannot list users") }
        }
    }

    data class ScreenData(
        val topBarText: Int,
        val buttonText: Int,
        val buttonTextAlternative: Int = R.string.nothing,
        val dialogTitle: Int = R.string.nothing,
        val dialogTitleAlternative: Int = R.string.nothing,
        val dialogButton2Function: (user: User) -> Unit = {},
        val dialogButton2FunctionAlternative: (user: User) -> Unit = {},
        val alternative: (user: User) -> Boolean = { false },
        val getUsersFunction: () -> Unit = {}
    )
}

enum class SearchMode {
    ADD_ADMIN,
    BLOCK_USERS,
    INVITE_USERS,
    NONE
}