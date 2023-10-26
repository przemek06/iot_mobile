package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto

data class ProjectDetailsUiState(
    val selectedTabIndex: Int,
    val user: User,
    val userProjectRole: UserProjectRole,
    val userRoleDescriptionId: Int?,
    val userOptionsList: List<MenuOption>,
    val menuOptionsList: List<MenuOption>,
    val dashboards: List<Dashboard>,
    val topics: List<Topic>,
    val members: List<User>
) {
    companion object {
        fun default(
            selectedTabIndex: Int = 0,
            user: User = mockUser,  // TODO: nullable
            userProjectRole: UserProjectRole = UserProjectRole.View,
            userRoleDescriptionId: Int? = null,
            userOptionsList: List<MenuOption> = emptyList(),
            menuOptionsList: List<MenuOption> = emptyList(),
            dashboards: List<Dashboard> = emptyList(),
            topics: List<Topic> = emptyList(),
            members: List<User> = emptyList(),
        ) = ProjectDetailsUiState(
            selectedTabIndex = selectedTabIndex,
            user = user,
            userProjectRole = userProjectRole,
            userRoleDescriptionId = userRoleDescriptionId,
            userOptionsList = userOptionsList,
            dashboards = dashboards,
            topics = topics,
            members = members,
            menuOptionsList = menuOptionsList
        )
    }
}

data class Topic(
    val id: Int,
    val title: String,
    val name: String,
    val dataType: TopicDataType
) {
    companion object {
        fun TopicDto.toTopic(): Topic? {
            val locId = id ?: return null
            return Topic(
                id = locId,
                title = name,
                name = uniqueName,
                dataType = valueType
            )
        }
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