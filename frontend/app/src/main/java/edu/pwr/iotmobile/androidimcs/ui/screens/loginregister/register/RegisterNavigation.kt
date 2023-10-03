package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.ui.navigation.Screen

interface RegisterNavigation {
    fun onLogin()

    companion object {
        fun default(
            navController: NavController
        ) = object : RegisterNavigation {
            override fun onLogin() {
                navController.navigate(Screen.Login.path)
            }

        }
    }
}