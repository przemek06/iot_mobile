package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen

interface AccountNavigation {

    fun openChangePassword()

    companion object {
        fun default(navController: NavHostController)
                = object : AccountNavigation {
            override fun openChangePassword() {
                navController.navigate(Screen.ChangePassword.path)
            }
        }
    }
}