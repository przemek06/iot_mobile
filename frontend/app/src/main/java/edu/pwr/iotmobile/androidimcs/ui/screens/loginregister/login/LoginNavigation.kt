package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.data.ActivateAccountType
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen
import edu.pwr.iotmobile.androidimcs.ui.navigation.appendArguments

interface LoginNavigation {
    fun onForgotPassword()
    fun onCreateAccount()
    fun openMainScreen()
    fun openAccountInactiveScreen(email: String)

    companion object {
        fun default(
            navController: NavController
        ) = object : LoginNavigation {
            override fun onForgotPassword() {
                navController.navigate(Screen.ForgotPassword.path)
            }

            override fun onCreateAccount() {
                navController.navigate(Screen.Register.path)
            }

            override fun openMainScreen() {
                navController.navigate(Screen.Main.path)
            }

            override fun openAccountInactiveScreen(email: String) {
                navController.navigate(
                    Screen.ActivateAccount.path.appendArguments(email, ActivateAccountType.AfterLogin)
                )
            }

        }
    }
}