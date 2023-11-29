package edu.pwr.iotmobile.androidimcs.ui.screens.main

import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments

interface MainScreenNavigation {
    fun openInvitations()
    fun openDashboardScreen(projectId: Int, dashboardId: Int, dashboardName: String)

    fun openProjects()
    fun openAdmin()

    companion object {
        fun default(navController: NavHostController) =
            object : MainScreenNavigation {
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

                override fun openAdmin() {
                    navController.navigate(Screen.Admin.path)
                }
            }
    }
}