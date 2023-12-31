package edu.pwr.iotmobile.androidimcs.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import edu.pwr.iotmobile.androidimcs.R

sealed class Screen(
    val path: String,
    @DrawableRes val icon: Int? = null,
    @StringRes val description: Int? = null,
    @StringRes val label: Int? = null,
    val tag: String = ""
) {
    object Main : Screen(
        path = "main",
        icon = R.drawable.ic_home,
        description = R.string.main_screen,
        label = R.string.home,
        tag = "main"
    )

    object Projects : Screen(
        path = "projects",
        icon = R.drawable.ic_project,
        description = R.string.projects_screen,
        label = R.string.projects,
        tag = "projects"
    )

    object Account : Screen(
        path = "account",
        icon = R.drawable.ic_account,
        description = R.string.account_screen,
        label = R.string.account,
        tag = "account"
    )

    object ProjectDetails : Screen("projectDetails/{arguments}")
    object AddTopic : Screen("addTopic/{arguments}")

    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgotPassword")
    object RegisterConfirmation : Screen("registerConfirmation")
    object ActivateAccount : Screen("activateAccount/{arguments}")
    object ChangePassword : Screen("changePassword")
    object Admin : Screen("admin")
    object Search : Screen("search/{arguments}")

    object Dashboard : Screen("dashboard/{arguments}")
    object AddComponent : Screen("addComponent/{arguments}")

    object Invitations : Screen("invitations")
    object Learn : Screen("learn")
}


