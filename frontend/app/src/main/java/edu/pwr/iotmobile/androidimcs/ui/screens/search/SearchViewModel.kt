package edu.pwr.iotmobile.androidimcs.ui.screens.search

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.User.Companion.toUser
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDtoSend
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto.Companion.toUserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto.Companion.toDto
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SearchViewModel(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(
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
                },
                getUsersFunction = { getAllUsers() },
                excludeUsersFunction = { excludeAdmins() }
            )
            SearchMode.ADD_PROJECT_ADMIN -> ScreenData(
                topBarText = R.string.add_admin,
                buttonText = R.string.add_admin,
                dialogTitle = R.string.search_admin_1,
                dialogButton2Function = {
                    addProjectAdmin(
                        userId = it.id,
                        projectId = navigation.projectId
                    )
                    setDialogInvisible()
                },
                getUsersFunction = { getProjectUsers(navigation.projectId) },
                excludeUsersFunction = { excludeProjectsAdmins(navigation.projectId) }
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
                },
                getUsersFunction = { getAllUsers() }
            )
            SearchMode.INVITE_USERS -> ScreenData(
                topBarText = R.string.invite_users,
                buttonText = R.string.invite,
                dialogTitle = R.string.sure_invite,
                buttonFunction = {
                    inviteUser(
                        userId = it.id,
                        projectId = navigation.projectId
                    )
                },
                getUsersFunction = { getAllUsers() },
                excludeUsersFunction = { excludeProjectUsers(navigation.projectId) }
            )
            SearchMode.REVOKE_ACCESS -> ScreenData(
                topBarText = R.string.revoke_access,
                buttonText = R.string.remove,
                dialogTitle = R.string.u_sure_revoke_access,
                dialogButton2Function = {
                    revokeAccess(
                        userId = it.id,
                        projectId = navigation.projectId
                    )
                    setDialogInvisible()
                },
                getUsersFunction = { getProjectUsers(navigation.projectId) },
                excludeUsersFunction = { excludeProjectsAdmins(navigation.projectId) }
            )
            SearchMode.EDIT_ROLES -> ScreenData(
                topBarText = R.string.edit_roles,
                buttonText = R.string.edit,
                buttonFunction = {
                    _uiState.value.selectedUser?.let { selectedUser ->
                        selectRole(_uiState.value.userRoles.firstOrNull { dto ->
                            dto.user.name == selectedUser.displayName
                        }?.toUserProjectRole() ?: UserProjectRole.VIEWER)
                    }
                    null
                },
                dialogTitle = R.string.edit_role_dialog,
                dialogButton2Function = {
                    editRole(
                        user = it,
                        projectId = navigation.projectId
                    )
                    setDialogInvisible()
                },
                getUsersFunction = {
                    getProjectUsers(navigation.projectId)
                    getRoles(navigation.projectId)
                }
            )
            else -> ScreenData(
                topBarText = R.string.nothing,
                buttonText = R.string.nothing
            )
        }
        _uiState.update {
            it.copy(data = data)
        }
        updateList()
    }
    private fun updateList() {
        val data = uiState.value.data
        data.getUsersFunction()
        data.excludeUsersFunction()
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
    fun selectRole(role: UserProjectRole) {
        _uiState.update {
            it.copy(selectedRole = role)
        }
    }

    private fun addAdmin(user: User) {
        _uiState.update { it.copy(isDialogLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository.updateUserRole(
                    id = user.id,
                    role = UserRole.ADMIN_ROLE.name
                )
            }.onSuccess {
                if (it.isSuccess) {
                    toast.toast("Successfully added admin")
                    updateList()
                } else {
                    toast.toast("Operation failed.")
                }
            }.onFailure {
                toast.toast("Operation failed.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    private fun addProjectAdmin(userId: Int, projectId: Int?) {
        if (projectId == null) {
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isDialogLoading = true) }

        viewModelScope.launch {
            try {
                val result = projectRepository.addProjectAdmin(
                    userId = userId,
                    projectId = projectId
                )
                if (result.isSuccess) {
                    toast.toast("Successfully added admin")
                    _uiState.update { it.copy(isDialogLoading = false) }
                    setDialogInvisible()
                    return@launch
                }
                toast.toast("Could not add admin.")
            } catch (e: Exception) {
                Log.e("Search", "Could not add admin.", e)
                toast.toast("Could not add admin.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    fun toggleBlockUser(user: User) {
        _uiState.update { it.copy(isDialogLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository.toggleUserBlocked(user.id)
            }.onSuccess {
                if (it.isSuccess) {
                    toast.toast("Success")
                    updateList()
                } else {
                    toast.toast("Operation failed.")
                }
            }.onFailure {
                toast.toast("Operation failed.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    private fun inviteUser(userId: Int, projectId: Int?) {
        if (projectId == null) {
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isDialogLoading = true) }

        viewModelScope.launch {
            try {
                val result = projectRepository.createInvitation(
                    InvitationDtoSend(
                        project = ProjectDto(
                            id = projectId,
                            name = "",
                            createdBy = 0
                        ),
                        userId = userId
                    )
                )
                if (result.isSuccess) {
                    toast.toast("Successfully invited user.")
                    _uiState.update { it.copy(isDialogLoading = false) }
                    setDialogInvisible()
                    return@launch
                }
                toast.toast("Could not invite user.")
            } catch (e: Exception) {
                Log.e("Search", "Could not invite user", e)
                toast.toast("Could not invite user.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    private fun revokeAccess(userId: Int, projectId: Int?) {
        if (projectId == null) {
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isDialogLoading = true) }

        viewModelScope.launch {
            try {
                val result = projectRepository.revokeAccess(
                    userId = userId,
                    projectId = projectId
                )
                if (result.isSuccess) {
                    toast.toast("Successfully revoked access")
                    _uiState.update { it.copy(isDialogLoading = false) }
                    setDialogInvisible()
                    return@launch
                }
                toast.toast("Could not revoke access to user.")
            } catch (e: Exception) {
                Log.e("Search", "Could not revoke access to user.", e)
                toast.toast("Could not revoke access to user.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    private fun editRole(user: User, projectId: Int?) {
        if (projectId == null) {
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isDialogLoading = true) }

        viewModelScope.launch {
            try {
                val projectRoleDto = ProjectRoleDto(
                    projectId = projectId,
                    user = user.toDto(),
                    role = _uiState.value.selectedRole.name
                )
                val result = projectRepository.editProjectRole(projectRoleDto)
                if (result.isSuccess) {
                    toast.toast("Successfully changed role.")
                    _uiState.update { it.copy(isDialogLoading = false) }
                    setDialogInvisible()
                    getRoles(projectId)
                    return@launch
                }
                toast.toast("Could not user change role.")
            } catch (e: Exception) {
                Log.e("Search", "Could not user change role.", e)
                toast.toast("Could not user change role.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }
    private fun getRoles(projectId: Int?) {
        if (projectId == null) {
            _uiState.update { it.copy(isError = true) }
            return
        }
        viewModelScope.launch {
            try {
                val result = projectRepository.findAllProjectRolesByProjectId(projectId)
                result.onSuccess { list ->
                    _uiState.update {
                        it.copy(
                            userRoles = list,
                            isError = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Search", "Could not get user roles.", e)
                toast.toast("Operation failed")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    private fun getAllUsers() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = userRepository.getAllUserInfo()
            result.onSuccess { list ->
                val users = list.mapNotNull { it.toUser() }
                _uiState.update { state ->
                    state.copy(
                        users = users,
                        searchedUsers = users.filter { !state.excludedUsers.contains(it) },
                        isError = false,
                        isLoading = false
                    )
                }
            }
            result.onFailure {
                toast.toast("Cannot list users")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }
    private fun getProjectUsers(projectId: Int?) {
        if (projectId == null) {
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val list = projectRepository.getUsersByProjectId(projectId)
                val users = list.mapNotNull { it.toUser() }

                _uiState.update { state ->
                    state.copy(
                        users = users,
                        searchedUsers = users.filter { !state.excludedUsers.contains(it) },
                        isError = false,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("Search", "Cannot get project users", e)
                _uiState.update { it.copy(
                    isLoading = false,
                    isError = true
                ) }
            }
        }
    }

    private fun excludeProjectUsers(projectId: Int?) {
        projectId?.let {
            viewModelScope.launch {
                kotlin.runCatching {
                    val list = projectRepository.getUsersByProjectId(projectId)
                    list.mapNotNull { it.toUser() }
                }.onSuccess { excludedUsers ->
                    _uiState.update { ui ->
                        ui.copy(
                            searchedUsers = ui.users.filter { !excludedUsers.contains(it) },
                            excludedUsers = excludedUsers
                        )
                    }
                }
            }
        }
    }

    private fun excludeProjectsAdmins(projectId: Int?) {
        projectId?.let {
            viewModelScope.launch {
                kotlin.runCatching {
                    val roles = projectRepository.findAllProjectRolesByProjectId(it).getOrNull() ?: return@launch
                    val users = projectRepository.getUsersByProjectId(projectId)
                    users.filter { user ->
                        roles.find { it.user == user }?.role == UserProjectRole.ADMIN.name
                    }.mapNotNull { it.toUser() }
                }.onSuccess { excludedUsers ->
                    _uiState.update { ui ->
                        ui.copy(
                            searchedUsers = ui.users.filter { !excludedUsers.contains(it) },
                            excludedUsers = excludedUsers
                        )
                    }
                }
            }
        }
    }

    private fun excludeAdmins() {
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository
                    .getAllUserInfo()
                    .getOrNull()
                    ?.filter { it.role == UserRole.ADMIN_ROLE.name }
                    ?.mapNotNull { it.toUser() }
                    ?: emptyList()
            }.onSuccess { excludedUsers ->
                _uiState.update { ui ->
                    ui.copy(
                        searchedUsers = ui.users.filter { !excludedUsers.contains(it) },
                        excludedUsers = excludedUsers
                    )
                }
            }
        }
    }

    data class ScreenData(
        val topBarText: Int,
        val buttonText: Int,
        val buttonTextAlternative: Int = R.string.nothing,
        val buttonFunction: (user: User) -> Any? = { null },
        val dialogTitle: Int = R.string.nothing,
        val dialogTitleAlternative: Int = R.string.nothing,
        val dialogContent: @Composable () -> Unit = {},
        val dialogButton2Function: (user: User) -> Unit = {},
        val dialogButton2FunctionAlternative: (user: User) -> Unit = {},
        val alternative: (user: User) -> Boolean = { false },
        val getUsersFunction: () -> Unit = {},
        val excludeUsersFunction: () -> Unit = {}
    )
}

enum class SearchMode {
    ADD_ADMIN,
    ADD_PROJECT_ADMIN,
    BLOCK_USERS,
    INVITE_USERS,
    REVOKE_ACCESS,
    EDIT_ROLES,
    NONE
}