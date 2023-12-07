package edu.pwr.iotmobile.androidimcs.ui.screens.search

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.extensions.asEnum
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface SearchNavigation {

    val mode: SearchMode
    val projectId: Int?

    fun goBack()

    companion object {
        fun default(
            navBackStackEntry: NavBackStackEntry,
            navController: NavHostController
        ) = object : SearchNavigation {
            override val mode: SearchMode
                get() = navBackStackEntry.getArguments().getOrElse(0) { "NONE" }.asEnum<SearchMode>() ?: SearchMode.NONE
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(1)?.toIntOrNull()

            override fun goBack() {
                navController.popBackStack()
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "userResultStatus",
                    true
                )
            }
        }
    }
}