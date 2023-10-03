package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen

interface LoginNavigation {
    fun onForgotPassword()
    fun onCreateAccount()

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

        }
    }
}