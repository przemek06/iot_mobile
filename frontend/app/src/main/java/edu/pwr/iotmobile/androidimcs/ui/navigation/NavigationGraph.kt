package edu.pwr.iotmobile.androidimcs.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        composable(Screen.Main.path) {
            MainScreen(
                navigation = MainScreenNavigation.default(navController)
            )
        }

        composable(Screen.Projects.path) {
            ProjectsScreen(
                navigation = ProjectsNavigation.default(navController)
            )
        }

        composable(Screen.Account.path) {
            AccountScreen(
                navigation = AccountNavigation.default(navController)
            )
        }

        composable(Screen.ChangePassword.path) {
            ChangePasswordScreen(
                navigation = ChangePasswordNavigation.default(navController)
            )
        }

        composable(Screen.ProjectDetails.path) { navBackStackEntry ->
            ProjectDetailsScreen(
                navigation = ProjectDetailsNavigation.default(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry
                )
            )
        }

        composable(Screen.AddTopic.path) {
            AddTopicScreen(navigation = AddTopicNavigation.default(
                navController = navController,
                navBackStackEntry = it
            ))
        }

        composable(Screen.Login.path) {
            LoginScreen(
                navigation = LoginNavigation.default(navController)
            )
        }

        composable(Screen.Register.path) {
            RegisterScreen(
                navigation = RegisterNavigation.default(navController)
            )
        }

        composable(Screen.ForgotPassword.path) {
            ForgotPasswordScreen(
                navigation = ForgotPasswordNavigation.default(navController)
            )
        }

        composable(Screen.ActivateAccount.path) {
            ActivateAccountScreen(
                navigation = ActivateAccountNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }

        composable(Screen.Admin.path) {
            AdminScreen(navigation = AdminNavigation.default(navController))
        }

        composable(Screen.Search.path) {
            SearchScreen(
                navigation = SearchNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }

        composable(Screen.Dashboard.path) {
            DashboardScreen(
                navigation = DashboardNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }

        composable(Screen.AddComponent.path) {
            AddComponentScreen(
                navigation = AddComponentNavigation.default(
                    navController = navController,
                    navBackStackEntry = it
                )
            )
        }
        composable(Screen.Invitations.path) {
            InvitationsScreen(
                navigation = InvitationsNavigation.default(navController)
            )
        }
        composable(Screen.Invitations.path) {
            InvitationsScreen(
                navigation = InvitationsNavigation.default(navController)
            )
        }
    }
}