package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface AddTopicNavigation {
    val projectId: Int?

    fun openProjectDetails()
    fun goBack()

    companion object {
        fun default(
            navController: NavHostController,
            navBackStackEntry: NavBackStackEntry
        ) = object : AddTopicNavigation{
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()

            override fun openProjectDetails() {
                projectId?.let {
                    navController.navigate(Screen.ProjectDetails.path.appendArguments(it, true))
                }
            }

            override fun goBack() {
                navController.popBackStack()
            }
        }
    }
}