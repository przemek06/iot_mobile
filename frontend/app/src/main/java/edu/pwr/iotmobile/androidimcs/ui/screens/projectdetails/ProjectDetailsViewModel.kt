package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.MenuOption
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val mockUser = User(
    id = "1",
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.Modify
)

class ProjectDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectDetailsUiState.default())
    val uiState = _uiState.asStateFlow()

    fun init(navigation: ProjectDetailsNavigation) {
        _uiState.update {
            it.copy(
                dashboards = listOf(1,2,3),
                topics = listOf(1,2,3),
                user = mockUser,
                members = listOf(mockUser, mockUser, mockUser),
                userRoleDescriptionId = mockUser.getUserRoleDescription(),
                userOptionsList = mockUser.generateUserOptions(navigation),
                menuOptionsList = mockUser.generateMenuOptions()
            )
        }
    }

    fun setSelectedTabIndex(index: Int) {
        _uiState.update {
            it.copy(selectedTabIndex = index)
        }
    }

    private fun User.getUserRoleDescription() = when (role) {
        UserRole.Admin -> R.string.admin_desc
        UserRole.Modify -> R.string.modify_desc
        UserRole.View -> R.string.view_desc
    }

    private fun User.generateUserOptions(navigation: ProjectDetailsNavigation) = when (role) {
        UserRole.Admin -> listOf(
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
        UserRole.Modify -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { /*TODO*/}
            )
        )
        UserRole.View -> listOf(
            MenuOption(
                titleId = R.string.leave_group,
                isBold = true,
                onClick = { /*TODO*/}
            )
        )
    }

    private fun User.generateMenuOptions() = when (role) {
        UserRole.Admin -> listOf(
            MenuOption(
                titleId = R.string.delete_project,
                onClick = {/*TODO*/}
            )
        )
        else -> emptyList()
    }
}