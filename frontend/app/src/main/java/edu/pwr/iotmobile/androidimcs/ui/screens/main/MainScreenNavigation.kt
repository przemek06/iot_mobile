package edu.pwr.iotmobile.androidimcs.ui.screens.main

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments

interface MainScreenNavigation {
    val isDashboardDeleted: Boolean
    fun openInvitations()
    fun openDashboardScreen(projectId: Int, dashboardId: Int, dashboardName: String)
    fun openProjects()
    fun openLearn()

    companion object {
        fun default(
            navController: NavHostController,
            navBackStackEntry: NavBackStackEntry
        ) =
            object : MainScreenNavigation {
                override val isDashboardDeleted: Boolean
                    get() = navBackStackEntry.savedStateHandle.getLiveData<Boolean>("resultStatus").value ?: false

                override fun openInvitations() {
                    navController.navigate(Screen.Invitations.path)
                }

                override fun openDashboardScreen(
                    projectId: Int,
                    dashboardId: Int,
                    dashboardName: String
                ) {
                    navController.navigate(
                        Screen.Dashboard.path.appendArguments(
                            projectId,
                            dashboardId,
                            dashboardName
                        )
                    )
                }

                override fun openProjects() {
                    navController.navigate(Screen.Projects.path)
                }

                override fun openLearn() {
                    navController.navigate(Screen.Learn.path)
                }
            }
    }
}