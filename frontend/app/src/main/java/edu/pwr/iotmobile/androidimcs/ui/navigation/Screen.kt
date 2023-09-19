package edu.pwr.iotmobile.androidimcs.ui.navigation

sealed class Screen(
    val path: String,
    val icon: Int? = null,
    val description: Int? = null,
    val tag: String = ""
) {
    object Main : Screen(
        path = "main",
        icon = null,
        description = null,
        tag = "main"
    )

    object Projects : Screen(
        path = "projects",
        icon = null,
        description = null,
        tag = "projects"
    )

    object Account : Screen(
        path = "account",
        icon = null,
        description = null,
        tag = "account"
    )
}
