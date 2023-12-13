package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments
import org.koin.core.scope.ScopeID

interface AddComponentNavigation {
    val projectId: Int?
    val scopeID: ScopeID?
    val isTopicSuccess: Boolean

    fun onReturn()
    fun openAddNewTopic()

    companion object {
        fun default(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ) = object : AddComponentNavigation {
            override val projectId: Int?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.toIntOrNull()
            override val scopeID: ScopeID?
                get() = navBackStackEntry.getArguments().getOrNull(1)
            override val isTopicSuccess: Boolean
                get() = navBackStackEntry.savedStateHandle.getLiveData<Boolean>("resultStatus").value ?: false

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
            override val scopeID: ScopeID?
                get() = null
            override val isTopicSuccess: Boolean
                get() = false

            override fun onReturn() {}
            override fun openAddNewTopic() {}

        }
    }
}