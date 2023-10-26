package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

interface DashboardNavigation {
    val dashboardId: Int?
    val dashboardName: String?

    fun onReturn()
    fun openAddComponentScreen()
}