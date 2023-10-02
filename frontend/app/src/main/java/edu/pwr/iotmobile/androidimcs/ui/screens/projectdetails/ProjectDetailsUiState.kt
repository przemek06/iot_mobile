package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.User

data class ProjectDetailsUiState(
    val selectedTabIndex: Int,
    val user: User,
    val userRoleDescriptionId: Int?,
    val userOptionsList: List<MenuOption>,
    val menuOptionsList: List<MenuOption>,
    val dashboards: List<Any>,
    val topics: List<Any>,
    val members: List<User>
) {
    companion object {
        fun default(
            selectedTabIndex: Int = 0,
            user: User = mockUser,  // TODO: nullable
            userRoleDescriptionId: Int? = null,
            userOptionsList: List<MenuOption> = emptyList(),
            menuOptionsList: List<MenuOption> = emptyList(),
            dashboards: List<Any> = emptyList(),
            topics: List<Any> = emptyList(),
            members: List<User> = emptyList(),
        ) = ProjectDetailsUiState(
            selectedTabIndex = selectedTabIndex,
            user = user,
            userRoleDescriptionId = userRoleDescriptionId,
            userOptionsList = userOptionsList,
            dashboards = dashboards,
            topics = topics,
            members = members,
            menuOptionsList = menuOptionsList
        )
    }
}