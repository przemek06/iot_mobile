package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface ProjectDetailsNavigation {
    val projectId: String?

    companion object {
        fun default(
            navController: NavHostController,
            navBackStackEntry: NavBackStackEntry
        ) =
            object : ProjectDetailsNavigation {
                override val projectId: String?
                    get() = navBackStackEntry.getArguments().getOrNull(0)

            }
    }
}