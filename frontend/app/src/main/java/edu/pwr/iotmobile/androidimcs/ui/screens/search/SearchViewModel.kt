package edu.pwr.iotmobile.androidimcs.ui.screens.search

import android.util.Log
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
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto.Companion.toDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult
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

    private var _projectId: Int? = null
    private var _searchMode: SearchMode? = null

    private var _allUsers: List<User> = emptyList()
    private var _projectRoles: List<ProjectRoleDto> = emptyList()

    fun init(
        searchMode: SearchMode?,
        projectId: Int?
    ) {
        if (searchMode == null || projectId == null) {
            _uiState.update { it.copy(isError = true) }
            return
        }

        if (searchMode != _searchMode || projectId != _projectId) {
            _projectId = projectId
            _searchMode = searchMode

            viewModelScope.launch {
                try {
                    val screenData = generateScreenData(
                        searchMode = searchMode,
                        projectId = projectId
                    ) ?: kotlin.run {
                        _uiState.update { it.copy(isError = true) }
                        return@launch
                    }

                    val filteredUsers = getFilteredUsers(searchMode, projectId)
                    _allUsers = filteredUsers

                    _uiState.update { ui ->
                        ui.copy(
                            data = screenData,
                            searchedUsers = filteredUsers,
                            isLoading = false,
                            isError = false
                        )
                    }
                } catch (e: Exception) {
                    Log.e("Search", "Failed to initialize", e)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            }
        }
    }

    fun onTextChange(text: String) {

        val searchedUsers = _allUsers.filter {
            it.displayName.contains(other = text, ignoreCase = true)
        }

        _uiState.update {
            it.copy(
                searchedText = text,
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

    private fun setRoleByUser(user: User) {
        val role = _projectRoles.firstOrNull {
            it.id == user.id
        }
        role?.toUserProjectRole()?.let {
            _uiState.update { ui ->
                ui.copy(selectedRole = it)
            }
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
                    updateUsersList()
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
                    updateUsersList()
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
                    updateUsersList()
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

                when (result) {
                    CreateResult.Success -> {
                        toast.toast("Successfully invited user.")
                        _uiState.update { it.copy(
                            isDialogLoading = false,
                            isError = false
                        ) }
                        setDialogInvisible()
                        updateUsersList()
                        return@launch
                    }

                    CreateResult.AlreadyExists -> {
                        toast.toast("User has already been invited.")
                    }

                    else -> {
                        toast.toast("Could not invite user.")
                    }
                }
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
                    updateUsersList()
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
                    updateUsersList()
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

    private fun updateUsersList() {
        val locSearchMode = _searchMode
        val locProjectId = _projectId
        if (locSearchMode == null || locProjectId == null) {
            Log.d("Search", "locSearchMode or locProjectId null")
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val filteredUsers = getFilteredUsers(locSearchMode, locProjectId)
                _allUsers = filteredUsers

                val searchedText = _uiState.value.searchedText
                val searchedUsers = if (searchedText.isBlank())
                    filteredUsers
                else filteredUsers.filter {
                    it.displayName.contains(other = searchedText, ignoreCase = true)
                }

                _uiState.update { ui ->
                    ui.copy(
                        searchedUsers = searchedUsers,
                        isLoading = false,
                        isError = false
                    )
                }
            } catch (e: Exception) {
                Log.e("Search", "Error while getting users list.", e)
                _uiState.update { ui ->
                    ui.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }

    }

    private suspend fun getFilteredUsers(
        searchMode: SearchMode,
        projectId: Int
    ): List<User> {
        val projectRoles = projectRepository.getAllProjectRolesByProjectId(projectId)
        _projectRoles = projectRoles
        val usersInProject = projectRepository.getUsersByProjectId(projectId)
        val users = getUsers(searchMode, projectId)

        return users.filterNot { user ->
            user.exclude(
                searchMode = searchMode,
                projectRole = projectRoles.find { user.id == it.user.id }
                    ?.toUserProjectRole(),
                usersInProject = usersInProject.map { it.id }
            )
        }.mapNotNull { it.toUser() }
    }

    private suspend fun getUsers(
        searchMode: SearchMode,
        projectId: Int
    ): List<UserInfoDto> {
        return when (searchMode) {
            SearchMode.ADD_ADMIN -> userRepository.getAllUserInfo()
            SearchMode.ADD_PROJECT_ADMIN -> projectRepository.getUsersByProjectId(projectId)
            SearchMode.BLOCK_USERS -> userRepository.getAllUserInfo()
            SearchMode.INVITE_USERS -> userRepository.getAllUserInfo()
            SearchMode.EDIT_ROLES -> projectRepository.getUsersByProjectId(projectId)
            SearchMode.REVOKE_ACCESS -> projectRepository.getUsersByProjectId(projectId)
        }
    }

    private fun UserInfoDto.exclude(
        searchMode: SearchMode,
        projectRole: UserProjectRole?,
        usersInProject: List<Int>
    ): Boolean {
        if (projectRole == null) return false
        return when (searchMode) {
            SearchMode.ADD_ADMIN -> this.role == UserRole.ADMIN_ROLE.name
            SearchMode.ADD_PROJECT_ADMIN -> projectRole == UserProjectRole.ADMIN
            SearchMode.BLOCK_USERS -> false
            SearchMode.INVITE_USERS -> this.id in usersInProject
            SearchMode.EDIT_ROLES -> false
            SearchMode.REVOKE_ACCESS -> projectRole == UserProjectRole.ADMIN
        }
    }

    private fun generateScreenData(
        searchMode: SearchMode?,
        projectId: Int?
    ): ScreenData? {
        if (searchMode == null || projectId == null) return null
        return when (searchMode) {
            SearchMode.ADD_ADMIN -> ScreenData(
                topBarText = R.string.add_admin,
                buttonText = R.string.add_admin,
                dialogTitle = R.string.search_admin_1,
                dialogButtonFunction = {
                    addAdmin(it)
                    setDialogInvisible()
                }
            )
            SearchMode.ADD_PROJECT_ADMIN -> ScreenData(
                topBarText = R.string.add_admin,
                buttonText = R.string.add_admin,
                dialogTitle = R.string.search_admin_1,
                dialogButtonFunction = {
                    addProjectAdmin(
                        userId = it.id,
                        projectId = projectId
                    )
                    setDialogInvisible()
                }
            )
            SearchMode.BLOCK_USERS -> ScreenData(
                topBarText = R.string.block_users,
                buttonText = R.string.block,
                buttonTextAlternative = R.string.unblock,
                dialogTitle = R.string.search_block_1,
                dialogTitleAlternative = R.string.search_block_2,
                dialogButtonFunction = {
                    toggleBlockUser(it)
                    setDialogInvisible()
                },
                isAlternative = {
                    it.isBlocked
                }
            )
            SearchMode.INVITE_USERS -> ScreenData(
                topBarText = R.string.invite_users,
                buttonText = R.string.invite,
                dialogTitle = R.string.sure_invite,
                buttonFunction = {
                    inviteUser(
                        userId = it.id,
                        projectId = projectId
                    )
                }
            )
            SearchMode.REVOKE_ACCESS -> ScreenData(
                topBarText = R.string.revoke_access,
                buttonText = R.string.remove,
                dialogTitle = R.string.u_sure_revoke_access,
                dialogButtonFunction = {
                    revokeAccess(
                        userId = it.id,
                        projectId = projectId
                    )
                    setDialogInvisible()
                }
            )
            SearchMode.EDIT_ROLES -> ScreenData(
                topBarText = R.string.edit_roles,
                buttonText = R.string.edit,
                buttonFunction = {
                    setRoleByUser(it)
                },
                dialogTitle = R.string.edit_role_dialog,
                dialogButtonFunction = {
                    editRole(
                        user = it,
                        projectId = projectId
                    )
                    setDialogInvisible()
                },
            )
        }
    }

    data class ScreenData(
        val topBarText: Int,
        val buttonText: Int,
        val buttonTextAlternative: Int = R.string.nothing,
        val buttonFunction: ((user: User) -> Unit)? = null,
        val dialogTitle: Int = R.string.nothing,
        val dialogTitleAlternative: Int = R.string.nothing,
        val dialogButtonFunction: (user: User) -> Unit = {},
        val isAlternative: (user: User) -> Boolean = { false },
    )
}

enum class SearchMode {
    ADD_ADMIN,
    ADD_PROJECT_ADMIN,
    BLOCK_USERS,
    INVITE_USERS,
    REVOKE_ACCESS,
    EDIT_ROLES
}