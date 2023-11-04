package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto.Companion.toUserProjectRole
import edu.pwr.iotmobile.androidimcs.data.ui.ProjectData.Companion.toProjectData
import edu.pwr.iotmobile.androidimcs.data.ui.Topic.Companion.toTopic
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.Dashboard.Companion.toDashboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ProjectDetVM"

val mockUser = User(
    id = 1,
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.USER_ROLE
)

class ProjectDetailsViewModel(
    private val dashboardRepository: DashboardRepository,
    private val topicRepository: TopicRepository,
    private val projectRepository: ProjectRepository,
    val toast: Toast
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectDetailsUiState.default())
    val uiState = _uiState.asStateFlow()

    private var projectId: Int? = null
    private var userProjectRole: UserProjectRole? = UserProjectRole.ADMIN

    fun init(navigation: ProjectDetailsNavigation) {
        // Only update UI if project id changed.
        if (projectId == null || projectId != navigation.projectId) {

            // Set private project id field
            val localProjectId = navigation.projectId ?: return
            projectId = localProjectId

            viewModelScope.launch {

                val projectUserInfo = projectRepository
                    .getUserProjectRole(localProjectId)
                    ?: return@launch

                val projectRole = projectUserInfo.toUserProjectRole() ?: return@launch
                userProjectRole = projectRole

                val projectData = projectRepository
                    .getProjectById(localProjectId)
                    ?.toProjectData()
                    ?: return@launch

                _uiState.update {
                    it.copy(
                        user = projectUserInfo.user,
                        userRoleDescriptionId = getUserRoleDescription(projectRole),
                        userProjectRole = projectRole,
                        userOptionsList = generateUserOptions(projectRole, navigation),
                        menuOptionsList = generateMenuOptions(projectRole),
                        dashboards = getDashboards(),
                        projectData = projectData
                    )
                }
            }
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
        val localProjectId = projectId ?: return
        viewModelScope.launch(Dispatchers.Default) {
            val dashboardDto = DashboardDto(
                name = name,
                projectId = localProjectId
            )
            kotlin.runCatching {
                dashboardRepository.createDashboard(dashboardDto)
            }.onSuccess {
                updateDashboards()
            }.onFailure {
                Log.d(TAG, "Add dashboard error")
            }
        }
    }

    fun deleteDashboard(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                dashboardRepository.deleteDashboard(id)
            }.onSuccess {
                updateDashboards()
            }.onFailure {
                Log.d(TAG, "Delete dashboard error")
            }
        }
    }

    fun deleteTopic(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                topicRepository.deleteTopic(id)
            }.onSuccess {
                updateTopics()
            }.onFailure {
                Log.d(TAG, "Delete topic error")
            }
        }
    }

    private fun updateDashboards() {
        viewModelScope.launch(Dispatchers.Default) {
            val dashboards = getDashboards()
            if (dashboards.isNotEmpty()) {
                _uiState.update { ui ->
                    ui.copy(dashboards = dashboards)
                }
            }
        }
    }

    private suspend fun getDashboards(): List<Dashboard> {
        val localProjectId = projectId ?: return emptyList()
        kotlin.runCatching {
            dashboardRepository.getDashboardsByProjectId(localProjectId)
        }.onSuccess { dashboards ->
            Log.d("null", "dashboards")
            dashboards.forEach {
                Log.d("null", it.toString())
            }
            return dashboards.mapNotNull { it.toDashboard() }
        }.onFailure {
            Log.d(TAG, "Get dashboards error")
            return emptyList()
        }
        return emptyList()
    }

    private fun updateTopics() {
        val localProjectId = projectId ?: return
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                topicRepository.getTopicsByProjectId(localProjectId)
            }.onSuccess { topics ->
                Log.d("null", "topics")
                topics.forEach {
                    Log.d("null", it.toString())
                }
                _uiState.update { ui ->
                    ui.copy(topics = topics.mapNotNull { it.toTopic() })
                }
            }.onFailure {
                Log.d(TAG, "Get topics error")
            }
        }
    }

    private fun updateUsers() {
        val localProjectId = projectId ?: return
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                projectRepository.getUsersByProjectId(localProjectId)
            }.onSuccess { users ->
                Log.d("null", "users")
                users.forEach {
                    Log.d("null", it.toString())
                }
                _uiState.update { ui ->
                    ui.copy(members = users)
                }
            }.onFailure {
                Log.d(TAG, "Get users error")
            }
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
                onClick = { /*TODO*/}
            ),
            MenuOption(
                titleId = R.string.edit_roles,
                onClick = { /*TODO*/}
            ),
            MenuOption(
                titleId = R.string.revoke_access,
                onClick = { /*TODO*/}
            ),
            MenuOption(
                titleId = R.string.add_admin,
                onClick = { /*TODO*/}
            )
        )
        UserProjectRole.EDITOR -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { /*TODO*/}
            )
        )
        UserProjectRole.VIEWER -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { /*TODO*/}
            )
        )
    }

    private fun generateMenuOptions(role: UserProjectRole) = when (role) {
        UserProjectRole.ADMIN -> listOf(
            MenuOption(
                titleId = R.string.delete_project,
                onClick = {/*TODO*/}
            )
        )
        else -> emptyList()
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
    fun setInfoVisible() {
        _uiState.update {
            it.copy(isInfoVisible = true)
        }
    }
    fun setInfoInvisible() {
        _uiState.update {
            it.copy(isInfoVisible = false)
        }
    }
    fun onTextChangeDashboard(text: String) {
        _uiState.update {
            it.copy(inputFieldDashboard = it.inputFieldDashboard.copy(text = text))
        }
    }

    enum class ProjectTab(val labelId: Int, val index: Int) {
        Dashboards(labelId = R.string.dashboards, index = 0),
        Topics(labelId = R.string.topics, index = 1),
        Group(labelId = R.string.group, index = 2)
    }
}