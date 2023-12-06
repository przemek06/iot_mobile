package edu.pwr.iotmobile.androidimcs.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent.AddComponentNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent.AddComponentScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.addtopic.AddTopicNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.addtopic.AddTopicScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.admin.AdminNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.admin.AdminScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.changepassword.ChangePasswordNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.changepassword.ChangePasswordScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.invitations.InvitationsNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.invitations.InvitationsScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate.ActivateAccountNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate.ActivateAccountScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword.ForgotPasswordNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword.ForgotPasswordScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register.RegisterNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register.RegisterScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.main.MainScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.main.MainScreenNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.search.SearchNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.search.SearchScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues = PaddingValues(10.dp),
    startDestination: String = Screen.Invitations.path
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        slidingComposable(Screen.Main.path) {
            MainScreen(
                navigation = MainScreenNavigation.default(navController, it)
            )
        }

        slidingComposable(Screen.Projects.path) {
            ProjectsScreen(
                navigation = ProjectsNavigation.default(navController)
            )
        }

        slidingComposable(Screen.Account.path) {
            AccountScreen(
                navigation = AccountNavigation.default(navController)
            )
        }

        slidingComposable(Screen.ChangePassword.path) {
            ChangePasswordScreen(
                navigation = ChangePasswordNavigation.default(navController)
            )
        }

        slidingComposable(Screen.ProjectDetails.path) { navBackStackEntry ->
            ProjectDetailsScreen(
                navigation = ProjectDetailsNavigation.default(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry
                )
            )
        }

        slidingComposable(Screen.AddTopic.path) {
            AddTopicScreen(navigation = AddTopicNavigation.default(
                navController = navController,
                navBackStackEntry = it
            ))
        }

        slidingComposable(Screen.Login.path) {
            LoginScreen(
                navigation = LoginNavigation.default(navController)
            )
        }

        slidingComposable(Screen.Register.path) {
            RegisterScreen(
                navigation = RegisterNavigation.default(navController)
            )
        }

        slidingComposable(Screen.ForgotPassword.path) {
            ForgotPasswordScreen(
                navigation = ForgotPasswordNavigation.default(navController)
            )
        }

        slidingComposable(Screen.ActivateAccount.path) {
            ActivateAccountScreen(
                navigation = ActivateAccountNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }

        slidingComposable(Screen.Admin.path) {
            AdminScreen(navigation = AdminNavigation.default(navController))
        }

        slidingComposable(Screen.Search.path) {
            SearchScreen(
                navigation = SearchNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }

        slidingComposable(Screen.Dashboard.path) {
            DashboardScreen(
                navigation = DashboardNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }

        slidingComposable(Screen.AddComponent.path) {
            AddComponentScreen(
                navigation = AddComponentNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }
        slidingComposable(Screen.Invitations.path) {
            InvitationsScreen(
                navigation = InvitationsNavigation.default(navController)
            )
        }
        slidingComposable(Screen.Invitations.path) {
            InvitationsScreen(
                navigation = InvitationsNavigation.default(navController)
            )
        }
    }
}

private fun NavGraphBuilder.slidingComposable(
    path: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = path,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }
    ) {
        content(this, it)
    }
}