package edu.pwr.iotmobile.androidimcs.ui.screens.main

import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen

interface MainScreenNavigation {
    fun openInvitations()

    companion object {
        fun default(navController: NavHostController) =
            object : MainScreenNavigation {
                override fun openInvitations() {
                    navController.navigate(Screen.Invitations.path)
                }
            }
    }
}