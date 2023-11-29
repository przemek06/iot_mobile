package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen

interface AdminNavigation {

    fun openAddAdmin()
    fun openBanUsers()
    fun openBannedUsers()

    fun openChangePassword()
    fun openLogin()

    companion object {
        fun default(navController: NavHostController)
                = object : AdminNavigation {
            override fun openAddAdmin() {
                //navController.navigate(Screen.AddAdmin.path)
            }
            override fun openBanUsers() {
                //navController.navigate(Screen.BanUsers.path)
            }
            override fun openBannedUsers() {
                //navController.navigate(Screen.BannedUsers.path)
            }

            override fun openChangePassword() {
                navController.navigate(Screen.ChangePassword.path)
            }

            override fun openLogin() {
                navController.navigate(Screen.Login.path)
            }
        }
    }
}