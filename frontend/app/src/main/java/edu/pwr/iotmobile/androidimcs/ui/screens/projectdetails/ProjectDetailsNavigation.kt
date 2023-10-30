package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface ProjectDetailsNavigation {
    val projectId: Int?

    fun openDashboardScreen()
    fun openAddTopic()
    fun onReturn()

    companion object {
        fun default(
            navController: NavHostController,
            navBackStackEntry: NavBackStackEntry
        ) = object : ProjectDetailsNavigation {
                override val projectId: Int?
                    get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()

                override fun openDashboardScreen() {
                    projectId?.let {
                        navController.navigate(Screen.Dashboard.path.appendArguments(it))
                    }
                }

                override fun openAddTopic() {
                    projectId?.let {
                        navController.navigate(Screen.AddTopic.path.appendArguments(it))
                    }
                }

                override fun onReturn() {
                    navController.popBackStack()
                }

            }
    }
}