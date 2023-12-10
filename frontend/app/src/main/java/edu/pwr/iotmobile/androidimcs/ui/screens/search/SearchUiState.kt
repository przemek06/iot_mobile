package edu.pwr.iotmobile.androidimcs.ui.screens.search

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole

data class SearchUiState (
    val searchedText: String,
    val searchedUsers: List<User>,
    val selectedUser: User?,
    val data: SearchViewModel.ScreenData,
    val selectedRole: UserProjectRole,
    val isDialogVisible: Boolean,
    val isLoading: Boolean = true,
    val isDialogLoading: Boolean = false,
    val isError: Boolean = false,
) {
     companion object {
         fun default(
             searchedText: String = "",
             searchedUsers: List<User> = emptyList(),
             selectedUser: User? = null,
             data: SearchViewModel.ScreenData = SearchViewModel.ScreenData(
                 topBarText = R.string.nothing,
                 buttonText = R.string.nothing
             ),
             isDialogVisible: Boolean = false,
             selectedRole: UserProjectRole = UserProjectRole.VIEWER
         ) = SearchUiState (
             searchedText = searchedText,
             searchedUsers = searchedUsers,
             selectedUser = selectedUser,
             data = data,
             isDialogVisible = isDialogVisible,
             selectedRole = selectedRole
         )
     }
}