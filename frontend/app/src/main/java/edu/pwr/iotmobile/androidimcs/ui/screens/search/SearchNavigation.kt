package edu.pwr.iotmobile.androidimcs.ui.screens.search

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface SearchNavigation {

    val mode: Int

    fun goBack()

    companion object {
        fun default(
            navBackStackEntry: NavBackStackEntry,
            navController: NavHostController
        ) = object : SearchNavigation {
            override val mode: Int
                get() = navBackStackEntry.getArguments().getOrElse(0) { "0" }.toInt()
            override fun goBack() {
                navController.popBackStack()
            }
        }
    }
}