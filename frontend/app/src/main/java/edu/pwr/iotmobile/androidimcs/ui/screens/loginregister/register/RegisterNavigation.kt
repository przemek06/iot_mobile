package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.data.ActivateAccountType
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments

interface RegisterNavigation {
    fun onLogin()
    fun openAccountInactiveScreen(email: String)

    companion object {
        fun default(
            navController: NavController
        ) = object : RegisterNavigation {
            override fun onLogin() {
                navController.navigate(Screen.Login.path)
            }

            override fun openAccountInactiveScreen(email: String) {
                navController.navigate(
                    Screen.ActivateAccount.path.appendArguments(email, ActivateAccountType.AfterRegistration)
                )
            }

        }
    }
}