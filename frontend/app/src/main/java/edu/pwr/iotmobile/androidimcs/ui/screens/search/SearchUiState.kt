package edu.pwr.iotmobile.androidimcs.ui.screens.search

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto

data class SearchUiState (
    val searchInputFieldData: String,
    val users: List<User>,
    val excludedUsers: List<User>,
    val searchedUsers: List<User>,
    val selectedUser: User?,
    val data: SearchViewModel.ScreenData,
    val isDialogVisible: Boolean,
    val userRoles: List<ProjectRoleDto>,
    val selectedRole: UserProjectRole
) {
     companion object {
         fun default(
             searchInputFieldData: String = "",
             users: List<User> = emptyList(),
             excludedUsers: List<User> = emptyList(),
             searchedUsers: List<User> = emptyList(),
             selectedUser: User? = null,
             data: SearchViewModel.ScreenData = SearchViewModel.ScreenData(
                 topBarText = R.string.nothing,
                 buttonText = R.string.nothing
             ),
             isDialogVisible: Boolean = false,
             userRoles: List<ProjectRoleDto> = emptyList(),
             selectedRole: UserProjectRole = UserProjectRole.VIEWER
         ) = SearchUiState (
             searchInputFieldData = searchInputFieldData,
             users = users,
             excludedUsers = excludedUsers,
             searchedUsers = searchedUsers,
             selectedUser = selectedUser,
             data = data,
             isDialogVisible = isDialogVisible,
             userRoles = userRoles,
             selectedRole = selectedRole
         )
     }
}