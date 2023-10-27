package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole

data class ProjectDetailsUiState(
    val selectedTabIndex: Int,
    val user: User,
    val userProjectRole: UserProjectRole,
    val userRoleDescriptionId: Int?,
    val userOptionsList: List<MenuOption>,
    val menuOptionsList: List<MenuOption>,
    val dashboards: List<Any>,
    val topics: List<Any>,
    val members: List<User>,
    val isDialogVisible: Boolean,
    val isInfoVisible: Boolean,
    val inputFieldDashboard: InputFieldData
) {
    companion object {
        fun default(
            selectedTabIndex: Int = 0,
            user: User = mockUser,  // TODO: nullable
            userProjectRole: UserProjectRole = UserProjectRole.View,
            userRoleDescriptionId: Int? = null,
            userOptionsList: List<MenuOption> = emptyList(),
            menuOptionsList: List<MenuOption> = emptyList(),
            dashboards: List<Any> = emptyList(),
            topics: List<Any> = emptyList(),
            members: List<User> = emptyList(),
            isDialogVisible: Boolean = false,
            isInfoVisible: Boolean = false,
            inputFieldDashboard: InputFieldData = InputFieldData()
        ) = ProjectDetailsUiState(
            selectedTabIndex = selectedTabIndex,
            user = user,
            userProjectRole = userProjectRole,
            userRoleDescriptionId = userRoleDescriptionId,
            userOptionsList = userOptionsList,
            dashboards = dashboards,
            topics = topics,
            members = members,
            menuOptionsList = menuOptionsList,
            isDialogVisible = isDialogVisible,
            inputFieldDashboard = inputFieldDashboard,
            isInfoVisible = isInfoVisible
        )
    }
}