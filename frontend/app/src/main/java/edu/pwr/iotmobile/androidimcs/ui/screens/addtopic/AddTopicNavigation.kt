package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import androidx.navigation.NavHostController

interface AddTopicNavigation {

    fun goBack()

    companion object {
        fun default(navController: NavHostController) = object : AddTopicNavigation{
            override fun goBack() {
                navController.popBackStack()
            }
        }
    }
}