package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments
import edu.pwr.iotmobile.androidimcs.ui.screens.search.SearchMode

interface ProjectDetailsNavigation {
    val projectId: Int?
    val startOnTopic: Boolean
    val isTopicSuccess: Boolean

    fun openDashboardScreen(dashboardId: Int, dashboardName: String)
    fun openAddTopic()
    fun openSearchInviteUsers()
    fun openSearchEditRoles()
    fun openSearchRevokeAccess()
    fun openSearchAddAdmin()
    fun onReturn()

    companion object {
        fun default(
            navController: NavHostController,
            navBackStackEntry: NavBackStackEntry
        ) = object : ProjectDetailsNavigation {
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()
            override val startOnTopic: Boolean
                get() = navBackStackEntry.getArguments().getOrNull(1)?.toBoolean() ?: false
            override val isTopicSuccess: Boolean
                get() = navBackStackEntry.savedStateHandle.getLiveData<Boolean>("resultStatus").value ?: false

            override fun openDashboardScreen(dashboardId: Int, dashboardName: String) {
                projectId?.let {
                    navController.navigate(Screen.Dashboard.path.appendArguments(it, dashboardId, dashboardName))
                }
            }

            override fun openAddTopic() {
                projectId?.let {
                    navController.navigate(Screen.AddTopic.path.appendArguments(it))
                }
            }

            override fun openSearchInviteUsers() {
                projectId?.let {
                    navController.navigate(Screen.Search.path.appendArguments(SearchMode.INVITE_USERS, it))
                }
            }

            override fun openSearchEditRoles() {
                projectId?.let {
                    navController.navigate(Screen.Search.path.appendArguments(SearchMode.EDIT_ROLES, it))
                }
            }

            override fun openSearchRevokeAccess() {
                projectId?.let {
                    navController.navigate(Screen.Search.path.appendArguments(SearchMode.REVOKE_ACCESS, it))
                }
            }

            override fun openSearchAddAdmin() {
                projectId?.let {
                    navController.navigate(Screen.Search.path.appendArguments(SearchMode.ADD_PROJECT_ADMIN, it))
                }
            }

            override fun onReturn() {
                navController.popBackStack()
            }
        }
    }
}