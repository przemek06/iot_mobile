package edu.pwr.iotmobile.androidimcs.ui.screens.changepassword

import androidx.navigation.NavHostController

interface ChangePasswordNavigation {

    fun goBack()

    companion object {
        fun default(navController: NavHostController)
                = object : ChangePasswordNavigation {
            override fun goBack() {
                navController.popBackStack()
            }
        }
    }
}