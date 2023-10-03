package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import edu.pwr.iotmobile.androidimcs.extensions.asEnum
import edu.pwr.iotmobile.androidimcs.ui.navigation.getArguments

interface ActivateAccountNavigation {
    val type: ActivateAccountType?
    fun onReturn()

    companion object {
        fun default(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        ) = object : ActivateAccountNavigation {
            override val type: ActivateAccountType?
                get() = navBackStackEntry.getArguments().getOrNull(0)?.asEnum<ActivateAccountType>()

            override fun onReturn() {
                navController.popBackStack()
            }

        }
    }
}

enum class ActivateAccountType {
    AfterLogin,
    AfterRegister
}