package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface DashboardNavigation {
    val dashboardId: Int?
    val dashboardName: String?

    fun onReturn()
    fun openAddComponentScreen()

    companion object {
        fun default(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ) = object : DashboardNavigation {
            override val dashboardId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()
            override val dashboardName: String?
                get() = navBackStackEntry.getArguments().getOrNull(1)

            override fun onReturn() {
                navController.popBackStack()
            }

            override fun openAddComponentScreen() {
                navController.navigate(Screen.AddComponent.path)
            }

        }
    }
}