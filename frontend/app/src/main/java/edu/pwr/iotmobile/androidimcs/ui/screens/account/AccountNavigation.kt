package edu.pwr.iotmobile.androidimcs.ui.screens.account

import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsNavigation

interface AccountNavigation {

    fun openChangePassword()
    fun openAccount()

    companion object {
        fun default(navController: NavHostController)
                = object : AccountNavigation {
            override fun openChangePassword() {
                navController.navigate(Screen.ChangePassword.path)
            }

            override fun openAccount() {
                navController.navigate(Screen.Account.path)
            }
        }
    }

}