package edu.pwr.iotmobile.androidimcs.ui.screens.search

import edu.pwr.iotmobile.androidimcs.data.User

data class SearchUiState (
    val searchInputFieldData: String,
    val users: List<User>,
    val searchedUsers: List<User>,
    val mode: Int
) {
     companion object {
         fun default(
             searchInputFieldData: String = "",
             users: List<User> = emptyList(),
             searchedUsers: List<User> = emptyList(),
             mode: Int = 0
         ) = SearchUiState (
             searchInputFieldData = searchInputFieldData,
             users = users,
             searchedUsers = searchedUsers,
             mode = mode
         )
     }
}