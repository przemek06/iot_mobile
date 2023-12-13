package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto
import edu.pwr.iotmobile.androidimcs.data.scopestates.ComponentsListState
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface DashboardNavigation {
    val projectId: Int?
    val dashboardId: Int?
    val dashboardName: String?

    fun onReturn(isDashboardDeleted: Boolean = false)
    fun openAddComponentScreen(componentListDto: ComponentListDto)

    companion object {
        fun default(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ) = object : DashboardNavigation {
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toInt()
            override val dashboardId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(1)?.toInt()
            override val dashboardName: String?
                get() = navBackStackEntry.getArguments().getOrNull(2)

            override fun onReturn(isDashboardDeleted: Boolean) {
                navController.popBackStack()
                if (isDashboardDeleted) {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "resultStatus",
                        true
                    )
                }
            }

            override fun openAddComponentScreen(componentListDto: ComponentListDto) {
                val scopeId = ComponentsListState.createScope(componentListDto)
                projectId?.let {
                    navController.navigate(Screen.AddComponent.path.appendArguments(it, scopeId))
                }
            }

        }
    }
}