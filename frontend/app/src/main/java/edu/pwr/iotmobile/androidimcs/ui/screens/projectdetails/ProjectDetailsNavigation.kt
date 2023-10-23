package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface ProjectDetailsNavigation {
    val projectId: String?

    fun openDashboardScreen(id: Int)

    companion object {
        fun default(
            navController: NavHostController,
            navBackStackEntry: NavBackStackEntry
        ) =
            object : ProjectDetailsNavigation {
                override val projectId: String?
                    get() = navBackStackEntry.getArguments().getOrNull(0)

                override fun openDashboardScreen(id: Int) {
                    navController.navigate(Screen.Dashboard.path.appendArguments(id))
                }

            }
    }
}