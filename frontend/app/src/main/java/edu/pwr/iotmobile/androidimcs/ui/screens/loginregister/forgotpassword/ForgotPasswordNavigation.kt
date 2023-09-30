package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword

import androidx.navigation.NavController

interface ForgotPasswordNavigation {
    fun onReturn()

    companion object {
        fun default(navController: NavController) =
            object  : ForgotPasswordNavigation {
                override fun onReturn() {
                    navController.popBackStack()
                }

            }
    }
}