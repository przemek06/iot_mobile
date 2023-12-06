package edu.pwr.iotmobile.androidimcs.ui.screens.learn

import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen

interface LearnScreenNavigation {
    fun onReturn()
    fun openProjects()

    companion object {
        fun default(
            navController: NavController
        ) = object : LearnScreenNavigation {
            override fun onReturn() {
                navController.popBackStack()
            }

            override fun openProjects() {
                navController.navigate(Screen.Projects.path)
            }

        }
    }
}