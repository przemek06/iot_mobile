package edu.pwr.iotmobile.androidimcs.ui.screens.search

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val mockUser = User(
    id = "1",
    displayName = "Alan Walker",
    email = "alan@walker.com",
    role = UserRole.Normal,
    isBlocked = true
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        val users = listOf(mockUser, mockUser, mockUser, mockUser)
        _uiState.update {
            it.copy(
                users = users,
                searchedUsers = users
            )
        }
    }

    fun init(mode: SearchMode) {
        val data = when(mode) {
            SearchMode.ADD_ADMIN -> ScreenData(
                topBarText = R.string.add_admin,
                buttonText = R.string.add_admin,
                dialogTitle = R.string.search_admin_1,
                dialogButton2Function = {
                    addAdmin(it)
                    setDialogInvisible()
                }
            )
            SearchMode.BLOCK_USERS -> ScreenData(
                topBarText = R.string.block_users,
                buttonText = R.string.block,
                buttonTextAlternative = R.string.unblock,
                dialogTitle = R.string.search_block_1,
                dialogTitleAlternative = R.string.search_block_2,
                dialogButton2Function = {
                    blockUser(it)
                    setDialogInvisible()
                },
                dialogButton2FunctionAlternative = {
                    unblockUser(it)
                    setDialogInvisible()
                },
                alternative = {
                    it.isBlocked
                }
            )
        }
        _uiState.update {
            it.copy(data = data)
        }
    }

    fun onTextChange(text: String) {

        val searchedUsers = uiState.value.users.filter {
            it.displayName.contains(other = text, ignoreCase = true)
        }

        _uiState.update {
            it.copy(
                searchInputFieldData = text,
                searchedUsers = searchedUsers
            )
        }
    }
    fun setSelectedUser(user: User) {
        _uiState.update {
            it.copy(selectedUser = user)
        }
    }
    fun setDialogVisible() {
        _uiState.update {
            it.copy(isDialogVisible = true)
        }
    }
    fun setDialogInvisible() {
        _uiState.update {
            it.copy(isDialogVisible = false)
        }
    }
    fun addAdmin(user: User) {
        _uiState.update { it.copy() }
    }
    fun blockUser(user: User) {
        _uiState.update {
            it.copy(searchedUsers = it.users.map { it.copy(isBlocked = true) })
        }
    }
    fun unblockUser(user: User) {
        _uiState.update {
            it.copy(searchedUsers = it.users.map { it.copy(isBlocked = false) })
        }
    }
}

enum class SearchMode(val mode: String) {
    ADD_ADMIN("AddAdmin"),
    BLOCK_USERS("BlockUsers")
}

data class ScreenData(
    val topBarText: Int,
    val buttonText: Int,
    val buttonTextAlternative: Int = R.string.nothing,
    val dialogTitle: Int = R.string.nothing,
    val dialogTitleAlternative: Int = R.string.nothing,
    val dialogButton2Function: (user: User) -> Unit = {},
    val dialogButton2FunctionAlternative: (user: User) -> Unit = {},
    val alternative: (user: User) -> Boolean = { false }
)