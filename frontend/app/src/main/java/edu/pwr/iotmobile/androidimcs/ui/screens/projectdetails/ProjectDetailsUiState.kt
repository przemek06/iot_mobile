package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.data.ui.ProjectData
import edu.pwr.iotmobile.androidimcs.data.ui.Topic

data class ProjectDetailsUiState(
    val selectedTabIndex: Int,
    val user: UserInfoDto,
    val userProjectRole: UserProjectRole,
    val roles: List<ProjectRoleDto>,
    val userRoleDescriptionId: Int?,
    val userOptionsList: List<MenuOption>,
    val menuOptionsList: List<MenuOption>,
    val dashboards: List<Dashboard>,
    val topics: List<Topic>,
    val members: List<UserInfoDto>,
    val isDialogVisible: Boolean,
    val isInfoVisible: Boolean,
    val inputFieldDashboard: InputFieldData,
    val projectData: ProjectData
) {
    companion object {
        fun default(
            selectedTabIndex: Int = 0,
            user: UserInfoDto = UserInfoDto.empty(),
            userProjectRole: UserProjectRole = UserProjectRole.VIEWER,
            roles: List<ProjectRoleDto> = emptyList(),
            userRoleDescriptionId: Int? = null,
            userOptionsList: List<MenuOption> = emptyList(),
            menuOptionsList: List<MenuOption> = emptyList(),
            dashboards: List<Dashboard> = emptyList(),
            topics: List<Topic> = emptyList(),
            members: List<UserInfoDto> = emptyList(),
            isDialogVisible: Boolean = false,
            isInfoVisible: Boolean = false,
            inputFieldDashboard: InputFieldData = InputFieldData(),
            projectData: ProjectData = ProjectData.empty()
        ) = ProjectDetailsUiState(
            selectedTabIndex = selectedTabIndex,
            user = user,
            userProjectRole = userProjectRole,
            roles = roles,
            userRoleDescriptionId = userRoleDescriptionId,
            userOptionsList = userOptionsList,
            dashboards = dashboards,
            topics = topics,
            members = members,
            menuOptionsList = menuOptionsList,
            isDialogVisible = isDialogVisible,
            inputFieldDashboard = inputFieldDashboard,
            isInfoVisible = isInfoVisible,
            projectData = projectData
        )
    }
}

data class Dashboard(
    val id: Int,
    val name: String,
) {
    companion object {
        fun DashboardDto.toDashboard(): Dashboard? {
            val locId = id ?: return null
            return Dashboard(
                id = locId,
                name = name
            )
        }
    }
}