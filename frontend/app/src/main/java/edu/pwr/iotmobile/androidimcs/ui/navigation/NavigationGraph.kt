package edu.pwr.iotmobile.androidimcs.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.main.MainScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsScreen
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues = PaddingValues(10.dp)
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.path,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Main.path) {
            MainScreen()
        }

        composable(Screen.Projects.path) {
            ProjectsScreen(
                navigation = ProjectsNavigation.default(navController)
            )
        }

        composable(Screen.Account.path) {
            AccountScreen()
        }

        composable(Screen.ProjectDetails.path) { navBackStackEntry ->
            ProjectDetailsScreen(
                navigation = ProjectDetailsNavigation.default(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry
                )
            )
        }

        composable(Screen.Login.path) {
            LoginScreen(
                navigation = LoginNavigation.default(navController)
            )
        }
    }
}