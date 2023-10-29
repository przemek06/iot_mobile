package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.UserRole
import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.Dashboard.Companion.toDashboard
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.Topic.Companion.toTopic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ProjectDetVM"

val mockUser = User(
    id = "1",
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.Normal
)

class ProjectDetailsViewModel(
    private val dashboardRepository: DashboardRepository,
    private val topicRepository: TopicRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectDetailsUiState.default())
    val uiState = _uiState.asStateFlow()

    private var projectId: Int? = null
    private var userProjectRole: UserProjectRole? = UserProjectRole.Admin

    fun init(navigation: ProjectDetailsNavigation) {
        if (projectId == null || projectId != navigation.projectId) {
            val localUserProjectRole = userProjectRole ?: return
            getDashboards()
            projectId = navigation.projectId
            _uiState.update {
                it.copy(
                    user = mockUser,
                    members = listOf(mockUser, mockUser, mockUser),
                    userRoleDescriptionId = getUserRoleDescription(localUserProjectRole),
                    userProjectRole = localUserProjectRole,
                    userOptionsList = generateUserOptions(localUserProjectRole, navigation),
                    menuOptionsList = generateMenuOptions(localUserProjectRole)
                )
            }
        }
    }

    fun setSelectedTabIndex(tab: ProjectTab) {
        when (tab) {
            ProjectTab.Dashboards -> getDashboards()
            ProjectTab.Topics -> getTopics()
            ProjectTab.Group -> { /*TODO*/ }
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
                getDashboards()
            }.onFailure {
                Log.d(TAG, "Add dashboard error")
            }
        }
    }

    fun addTopic(name: String, uniqueName: String, dataType: TopicDataType) {
        val localProjectId = projectId ?: return
        viewModelScope.launch(Dispatchers.Default) {
            val topicDto = TopicDto(
                name = name,
                uniqueName = uniqueName,
                valueType = dataType,
                projectId = localProjectId
            )
            kotlin.runCatching {
                topicRepository.createTopic(topicDto)
            }.onSuccess {
                getTopics()
            }.onFailure {
                Log.d(TAG, "Add topic error")
            }
        }
    }

    fun deleteDashboard(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                dashboardRepository.deleteDashboard(id)
            }.onSuccess {
                getDashboards()
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
                getTopics()
            }.onFailure {
                Log.d(TAG, "Delete topic error")
            }
        }
    }

    private fun getDashboards() {
        val localProjectId = projectId ?: return
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                dashboardRepository.getDashboardsByProjectId(localProjectId)
            }.onSuccess { dashboards ->
                _uiState.update { ui ->
                    ui.copy(dashboards = dashboards.mapNotNull { it.toDashboard() })
                }
            }.onFailure {
                Log.d(TAG, "Get dashboards error")
            }
        }
    }

    private fun getTopics() {
        val localProjectId = projectId ?: return
        viewModelScope.launch(Dispatchers.Default) {
            kotlin.runCatching {
                topicRepository.getTopicsByProjectId(localProjectId)
            }.onSuccess { topics ->
                _uiState.update { ui ->
                    ui.copy(topics = topics.mapNotNull { it.toTopic() })
                }
            }.onFailure {
                Log.d(TAG, "Get topics error")
            }
        }
    }

    private fun getUserRoleDescription(role: UserProjectRole) = when (role) {
        UserProjectRole.Admin -> R.string.admin_desc
        UserProjectRole.Editor -> R.string.modify_desc
        UserProjectRole.View -> R.string.view_desc
    }

    private fun generateUserOptions(
        role: UserProjectRole,
        navigation: ProjectDetailsNavigation
    ) = when (role) {
        UserProjectRole.Admin -> listOf(
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
        UserProjectRole.Editor -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { /*TODO*/}
            )
        )
        UserProjectRole.View -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { /*TODO*/}
            )
        )
    }

    private fun generateMenuOptions(role: UserProjectRole) = when (role) {
        UserProjectRole.Admin -> listOf(
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