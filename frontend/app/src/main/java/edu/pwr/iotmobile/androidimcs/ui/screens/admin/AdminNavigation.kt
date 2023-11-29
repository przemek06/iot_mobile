package edu.pwr.iotmobile.androidimcs.ui.screens.admin

import androidx.navigation.NavHostController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments
import edu.pwr.iotmobile.androidimcs.ui.screens.search.SearchMode

interface AdminNavigation {

    fun openAddAdmin()
    fun openBanUsers()

    fun openChangePassword()
    fun openLogin()

    companion object {
        fun default(navController: NavHostController)
                = object : AdminNavigation {
            override fun openAddAdmin() {
                navController.navigate(Screen.Search.path.appendArguments(SearchMode.ADD_ADMIN))
            }
            override fun openBanUsers() {
                navController.navigate(Screen.Search.path.appendArguments(SearchMode.BLOCK_USERS))
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