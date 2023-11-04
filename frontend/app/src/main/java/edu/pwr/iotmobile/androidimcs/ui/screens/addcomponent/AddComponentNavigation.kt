package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface AddComponentNavigation {
    val projectId: Int?

    fun onReturn()
    fun openAddNewTopic()

    companion object {
        fun default(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ) = object : AddComponentNavigation {
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()

            override fun onReturn() {
                navController.popBackStack()
            }

            override fun openAddNewTopic() {
                projectId?.let {
                    navController.navigate(Screen.AddTopic.path.appendArguments(it))
                }
            }

        }

        fun empty() = object : AddComponentNavigation {
            override val projectId: Int?
                get() = null

            override fun onReturn() {}
            override fun openAddNewTopic() {}

        }
    }
}