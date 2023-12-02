package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments

interface ProjectsNavigation {
    fun openProjectDetails(id: Int)
    fun goBack()

    companion object {
        fun default(navController: NavHostController)
        = object : ProjectsNavigation {
            override fun openProjectDetails(id: Int) {
                navController.navigate(Screen.ProjectDetails.path.appendArguments(id))
            }

            override fun goBack() {
                navController.popBackStack()
            }

        }
    }
}