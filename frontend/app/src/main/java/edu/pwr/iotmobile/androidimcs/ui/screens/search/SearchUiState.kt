package edu.pwr.iotmobile.androidimcs.ui.screens.search

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User

data class SearchUiState (
    val searchInputFieldData: String,
    val users: List<User>,
    val searchedUsers: List<User>,
    val selectedUser: User?,
    val data: ScreenData,
    val isDialogVisible: Boolean
) {
     companion object {
         fun default(
             searchInputFieldData: String = "",
             users: List<User> = emptyList(),
             searchedUsers: List<User> = emptyList(),
             selectedUser: User? = null,
             data: ScreenData = ScreenData(
                 topBarText = R.string.nothing,
                 buttonText = R.string.nothing
             ),
             isDialogVisible: Boolean = false
         ) = SearchUiState (
             searchInputFieldData = searchInputFieldData,
             users = users,
             searchedUsers = searchedUsers,
             selectedUser = selectedUser,
             data = data,
             isDialogVisible = isDialogVisible
         )
     }
}