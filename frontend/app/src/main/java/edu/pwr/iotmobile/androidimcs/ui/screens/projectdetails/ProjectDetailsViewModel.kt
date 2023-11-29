package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDeletedDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto.Companion.toUserProjectRole
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult
import edu.pwr.iotmobile.androidimcs.data.ui.ProjectData.Companion.toProjectData
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toTopic
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.listener.ProjectDeletedWebSocketListener
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.Dashboard.Companion.toDashboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

private const val TAG = "ProjectDetVM"

class ProjectDetailsViewModel(
    private val dashboardRepository: DashboardRepository,
    private val topicRepository: TopicRepository,
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    val toast: Toast,
    val event: Event,
    private val client: OkHttpClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectDetailsUiState.default())
    val uiState = _uiState.asStateFlow()

    private var _projectId: Int? = null
    private var _userProjectRole: UserProjectRole? = null

    private var projectDeletedListener: ProjectDeletedWebSocketListener? = null

    override fun onCleared() {
        projectDeletedListener?.closeWebSocket()
    }

    fun init(navigation: ProjectDetailsNavigation) {
        // Only update UI if project id changed.
        if (_projectId == null || _projectId != navigation.projectId) {
            _uiState.update { it.copy(isLoading = true) }

            // Set private project id field
            val localProjectId = navigation.projectId ?: return
            _projectId = localProjectId

            projectDeletedListener?.closeWebSocket()
            // Connect to project deleted listener
            projectDeletedListener = ProjectDeletedWebSocketListener(
                client = client,
                projectId = localProjectId,
                onProjectDeleted = { d -> onProjectDeleted(d) }
            )

            viewModelScope.launch {

                try {
                    val projectUserInfo = projectRepository
                        .getUserProjectRole(localProjectId)
                        ?: return@launch

                    val projectRole = projectUserInfo.toUserProjectRole() ?: return@launch
                    _userProjectRole = projectRole

                    val roles = projectRepository
                        .findAllProjectRolesByProjectId(localProjectId)
                        .getOrNull()
                        ?: emptyList()

                    val projectData = projectRepository
                        .getProjectById(localProjectId)
                        ?.toProjectData()
                        ?: run {
                            Log.d("ProjectDetails", "Failed to get project data")
                            _uiState.update { it.copy(
                                isError = true,
                                isLoading = false
                            ) }
                            return@launch
                        }

                    if (navigation.startOnTopic) {
                        updateTopics()
                    }

                    _uiState.update {
                        it.copy(
                            user = projectUserInfo.user,
                            userRoleDescriptionId = getUserRoleDescription(projectRole),
                            userProjectRole = projectRole,
                            roles = roles,
                            userOptionsList = generateUserOptions(projectRole, navigation),
                            menuOptionsList = generateMenuOptions(projectRole),
                            dashboards = getDashboards(),
                            projectData = projectData,
                            isLoading = false,
                            isError = false,
                            selectedTabIndex = if (navigation.startOnTopic)
                                ProjectTab.Topics.index
                            else ProjectTab.Dashboards.index
                        )
                    }
                } catch (e: Exception) {
                    Log.e("ProjectDetails", "Error while loading screen.", e)
                    _uiState.update { it.copy(
                        isError = true,
                        isLoading = false
                    ) }
                }
            }
        }
    }

    private fun onProjectDeleted(data: ProjectDeletedDto) {
        viewModelScope.launch {
            toast.toast("Project ${data.projectId} has been deleted.")
            event.event(PROJECT_DELETED_EVENT)
        }
    }

    fun setSelectedTabIndex(tab: ProjectTab) {
        when (tab) {
            ProjectTab.Dashboards -> updateDashboards()
            ProjectTab.Topics -> updateTopics()
            ProjectTab.Group -> updateUsers()
        }
        _uiState.update {
            it.copy(selectedTabIndex = tab.index)
        }
    }

    fun addDashboard(name: String) {
        val localProjectId = _projectId ?: run {
            viewModelScope.launch { toast.toast("Operation failed.") }
            return
        }

        if (name.isBlank()) {
            _uiState.update {
                it.copy(inputFieldDashboard = it.inputFieldDashboard.copy(
                    isError = true,
                    errorMessage = R.string.s62
                ))
            }
            return
        }

        _uiState.update { it.copy(isDialogLoading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            val dashboardDto = DashboardDto(
                name = name,
                projectId = localProjectId
            )
            kotlin.runCatching {
                dashboardRepository.createDashboard(dashboardDto)
            }.onSuccess { result ->
                when (result) {
                    CreateResult.Success -> updateDashboards()
                    CreateResult.AlreadyExists ->
                        _uiState.update {
                            it.copy(
                                inputFieldDashboard = it.inputFieldDashboard.copy(
                                    isError = true,
                                    errorMessage = R.string.s63
                                )
                            )
                        }
                    else -> toast.toast("Operation failed.")
                }
            }.onFailure {
                Log.d(TAG, "Add dashboard error")
                toast.toast("Operation failed.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    fun deleteTopic(id: Int) {
        _uiState.update { it.copy(isDialogLoading = true) }
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                topicRepository.deleteTopic(id)
            }.onSuccess {
                updateTopics()
            }.onFailure {
                Log.d(TAG, "Delete topic error")
                toast.toast("Could not delete topic.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    fun regenerateConnectionKey() {
        _uiState.update { it.copy(isDialogLoading = true) }
        viewModelScope.launch(Dispatchers.Default) {
            val localProjectId = _projectId ?: return@launch
            kotlin.runCatching {
                projectRepository.regenerateConnectionKey(localProjectId)
            }.onSuccess { result ->
                if (result.isSuccess) {
                    result.getOrNull()?.let { projectDto ->
                        val data = projectDto.toProjectData() ?: run {
                            toast.toast("Could not regenerate key.")
                            _uiState.update { it.copy(isDialogLoading = false) }
                            return@launch
                        }
                        _uiState.update {
                            it.copy(projectData = data)
                        }
                    }
                } else {
                    Log.d(TAG, "Regenerate key failed")
                    toast.toast("Could not regenerate key.")
                }
            }.onFailure {
                Log.d(TAG, "Regenerate key error")
                toast.toast("Could not regenerate key.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    private fun updateDashboards() {
        viewModelScope.launch(Dispatchers.Default) {
            val dashboards = getDashboards()
            if (dashboards.isNotEmpty()) {
                _uiState.update { ui ->
                    ui.copy(
                        dashboards = dashboards,
                        inputFieldDashboard = ui.inputFieldDashboard.copy(text = "")
                    )
                }
            }
        }
    }

    private suspend fun getDashboards(): List<Dashboard> {
        val localProjectId = _projectId ?: run {
            _uiState.update { it.copy(isError = true) }
            return emptyList()
        }
        _uiState.update { it.copy(isLoading = true) }

        kotlin.runCatching {
            dashboardRepository.getDashboardsByProjectId(localProjectId)
        }.onSuccess { dashboards ->
            _uiState.update { it.copy(
                isLoading = false,
                isError = false
            ) }
            return dashboards.mapNotNull { it.toDashboard() }
        }.onFailure {
            Log.e(TAG, "Get dashboards error", it)
        }
        _uiState.update { it.copy(
            isError = true,
            isLoading = false
        ) }
        return emptyList()
    }

    fun updateTopics() {
        val localProjectId = _projectId ?: run {
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                topicRepository.getTopicsByProjectId(localProjectId)
            }.onSuccess { topics ->
                _uiState.update { ui ->
                    ui.copy(
                        topics = topics.mapNotNull { it.toTopic() },
                        isLoading = false,
                        isError = false
                    )
                }
            }.onFailure { e ->
                Log.e(TAG, "Get topics error", e)
                _uiState.update { it.copy(
                    isError = true,
                    isLoading = false
                ) }
            }
        }
    }

    private fun updateUsers() {
        val localProjectId = _projectId ?: run {
            _uiState.update { it.copy(isError = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                projectRepository.getUsersByProjectId(localProjectId)
            }.onSuccess { users ->
                _uiState.update { ui ->
                    ui.copy(
                        members = users,
                        isLoading = false,
                        isError = false
                    )
                }
            }.onFailure { e ->
                Log.e(TAG, "Get users error", e)
                _uiState.update { it.copy(
                    isError = true,
                    isLoading = false
                ) }
            }
        }
    }

    fun deleteProject() {
        _uiState.update { it.copy(isDialogLoading = true) }
        viewModelScope.launch {
            val localProjectId = _projectId ?: return@launch
            kotlin.runCatching {
                projectRepository.deleteProject(localProjectId)
            }.onSuccess {
                updateDashboards()
            }.onFailure {
                Log.d(TAG, "Failed to delete project.")
            }
            _uiState.update { it.copy(isDialogLoading = false) }
        }
    }

    fun toggleDeleteProjectDialog() {
        _uiState.update {
            it.copy(isDeleteProjectDialogVisible = !it.isDeleteProjectDialogVisible)
        }
    }

    fun onTextChangeDashboard(text: String) {
        _uiState.update {
            it.copy(inputFieldDashboard = it.inputFieldDashboard.copy(text = text))
        }
    }

    private fun getUserRoleDescription(role: UserProjectRole) = when (role) {
        UserProjectRole.ADMIN -> R.string.admin_desc
        UserProjectRole.EDITOR -> R.string.modify_desc
        UserProjectRole.VIEWER -> R.string.view_desc
    }

    private fun generateUserOptions(
        role: UserProjectRole,
        navigation: ProjectDetailsNavigation
    ) = when (role) {
        UserProjectRole.ADMIN -> listOf(
            MenuOption(
                titleId = R.string.invite_users,
                isBold = true,
                onClick = { navigation.openSearchInviteUsers() }
            ),
            MenuOption(
                titleId = R.string.edit_roles,
                onClick = { navigation.openSearchEditRoles() }
            ),
            MenuOption(
                titleId = R.string.revoke_access,
                onClick = { navigation.openSearchRevokeAccess() }
            ),
            MenuOption(
                titleId = R.string.add_admin,
                onClick = { navigation.openSearchAddAdmin() }
            ),
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { leaveGroup() }
            )
        )
        UserProjectRole.EDITOR -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { leaveGroup() }
            )
        )
        UserProjectRole.VIEWER -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { leaveGroup() }
            )
        )
    }

    private fun generateMenuOptions(role: UserProjectRole) = when (role) {
        UserProjectRole.ADMIN -> listOf(
            MenuOption(
                titleId = R.string.delete_project,
                onClick = { toggleDeleteProjectDialog() }
            )
        )
        else -> emptyList()
    }

    private fun leaveGroup() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val projectId = _projectId ?: kotlin.run {
                _uiState.update { it.copy(
                    isError = true,
                    isLoading = false
                ) }
                return@launch
            }
            try {
                val userData = userRepository.getLoggedInUser().firstOrNull() ?: kotlin.run {
                    _uiState.update { it.copy(
                        isError = true,
                        isLoading = false
                    ) }
                    return@launch
                }
                val result = projectRepository.revokeAccess(
                    projectId = projectId,
                    userId = userData.id
                )
                result.onSuccess {
                    _uiState.update { it.copy(
                        isError = false,
                        isLoading = false
                    ) }
                    toast.toast("Successfully left project.")
                    return@launch
                }.onFailure {
                    _uiState.update { it.copy(
                        isError = true,
                        isLoading = false
                    ) }
                    toast.toast("Operation failed.")
                }
            } catch (e: Exception) {
                Log.e("ProjectDetails", "Error while leaving project.", e)
                _uiState.update { it.copy(
                    isError = true,
                    isLoading = false
                ) }
            }
        }
    }

    enum class ProjectTab(val labelId: Int, val index: Int) {
        Dashboards(labelId = R.string.dashboards, index = 0),
        Topics(labelId = R.string.topics, index = 1),
        Group(labelId = R.string.group, index = 2)
    }

    companion object {
        const val PROJECT_DELETED_EVENT = "projectDeleted"
    }
}