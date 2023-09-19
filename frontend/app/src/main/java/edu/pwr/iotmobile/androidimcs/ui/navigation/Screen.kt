package edu.pwr.iotmobile.androidimcs.ui.navigation

import edu.pwr.iotmobile.androidimcs.R

sealed class Screen(
    val path: String,
    val icon: Int? = null,
    val description: Int? = null,
    val tag: String = ""
) {
    object Main : Screen(
        path = "main",
        icon = R.drawable.ic_home,
        description = R.string.main_screen,
        tag = "main"
    )

    object Projects : Screen(
        path = "projects",
        icon = R.drawable.ic_project,
        description = R.string.projects_screen,
        tag = "projects"
    )

    object Account : Screen(
        path = "account",
        icon = R.drawable.ic_account,
        description = R.string.account_screen,
        tag = "account"
    )
}
