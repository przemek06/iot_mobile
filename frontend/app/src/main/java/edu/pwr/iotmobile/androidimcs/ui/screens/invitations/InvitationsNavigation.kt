package edu.pwr.iotmobile.androidimcs.ui.screens.invitations

import androidx.navigation.NavHostController

interface InvitationsNavigation {
    fun goBack()

    companion object {
        fun default(navController: NavHostController)
                = object : InvitationsNavigation {
            override fun goBack() {
                navController.popBackStack()
            }
        }
    }
}