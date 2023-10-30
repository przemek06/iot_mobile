package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface AddTopicNavigation {
    val projectId: Int?

    fun goBack()

    companion object {
        fun default(
            navController: NavHostController,
            navBackStackEntry: NavBackStackEntry
        ) = object : AddTopicNavigation{
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()

            override fun goBack() {
                navController.popBackStack()
            }
        }
    }
}