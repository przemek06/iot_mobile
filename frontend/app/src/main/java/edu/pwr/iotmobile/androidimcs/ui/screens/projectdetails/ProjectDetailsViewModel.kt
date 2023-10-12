package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val mockUser = User(
    id = "1",
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.Normal
)

class ProjectDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectDetailsUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: ProjectDetailsNavigation) {
        val userProjectRole = UserProjectRole.Admin
        _uiState.update {
            it.copy(
                dashboards = listOf(1,2,3),
                topics = listOf(1,2,3),
                user = mockUser,
                members = listOf(mockUser, mockUser, mockUser),
                userRoleDescriptionId = getUserRoleDescription(userProjectRole),
                userProjectRole = userProjectRole,
                userOptionsList = generateUserOptions(userProjectRole, navigation),
                menuOptionsList = generateMenuOptions(userProjectRole)
            )
        }
    }

    fun setSelectedTabIndex(index: Int) {
        _uiState.update {
            it.copy(selectedTabIndex = index)
        }
    }

    private fun getUserRoleDescription(role: UserProjectRole) = when (role) {
        UserProjectRole.Admin -> R.string.admin_desc
        UserProjectRole.Modify -> R.string.modify_desc
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
        UserProjectRole.Modify -> listOf(
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
}