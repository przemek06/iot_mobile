package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface AddComponentNavigation {
    val projectId: Int?

    fun onReturn()

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

        }

        fun empty() = object : AddComponentNavigation {
            override val projectId: Int?
                get() = null

            override fun onReturn() {}

        }
    }
}