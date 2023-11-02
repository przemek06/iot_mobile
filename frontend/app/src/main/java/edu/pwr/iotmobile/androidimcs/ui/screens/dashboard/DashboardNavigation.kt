package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface DashboardNavigation {
    val projectId: Int?
    val dashboardId: Int?
    val dashboardName: String?

    fun onReturn()
    fun openAddComponentScreen()

    companion object {
        fun default(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ) = object : DashboardNavigation {
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()
            override val dashboardId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(1)?.toInt()
            override val dashboardName: String?
                get() = navBackStackEntry.getArguments().getOrNull(2)

            override fun onReturn() {
                navController.popBackStack()
            }

            override fun openAddComponentScreen() {
                projectId?.let {
                    navController.navigate(Screen.AddComponent.path.appendArguments(it))
                }
            }

        }
    }
}