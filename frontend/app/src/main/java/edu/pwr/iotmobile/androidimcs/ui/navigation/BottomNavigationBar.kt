@file:OptIn(ExperimentalMaterial3Api::class)

package edu.pwr.iotmobile.androidimcs.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions

private val SCREENS_WITHOUT_BOTTOM_BAR = listOf(
    Screen.Login.path,
    Screen.Register.path,
    Screen.ActivateAccount.path,
    Screen.RegisterConfirmation.path,
    Screen.ForgotPassword.path,
    Screen.ChangePassword.path
)
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    startDestination: String
) {
    val bottomNavItems = listOf(
        Screen.Main,
        Screen.Projects,
        Screen.Account,
    )

    // Subscribe to navBackStackEntry, required to get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Log.d("route", currentDestination?.route ?: "no route")

    val bottomBarState = when (currentDestination?.route) {
        in SCREENS_WITHOUT_BOTTOM_BAR -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarState,
                enter = slideInVertically(initialOffsetY = { s -> s } ),
                exit = slideOutVertically(targetOffsetY = { t -> t } )
            ) {
                NavigationBar(modifier = Modifier.height(Dimensions.space60)) {
                    bottomNavItems.forEach { screen ->
                        val iconSize =
                            if (screen.path == Screen.Projects.path) Dimensions.space30
                            else Dimensions.space22
                        NavigationBarItem(
                            modifier = Modifier.testTag(screen.tag),
                            icon = {
                                if (screen.icon != null && screen.description != null)
                                    Image(
                                        modifier = Modifier.size(iconSize),
                                        painter = painterResource(id = screen.icon),
                                        contentDescription = stringResource(id = screen.description),
                                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                                    )
                            },
                            alwaysShowLabel = true,
                            selected = currentDestination?.hierarchy?.any { it.route == screen.path } == true,
                            onClick = {
                                navController.navigate(screen.path) { launchSingleTop = true }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            innerPadding = innerPadding,
            startDestination = startDestination
        )
    }
}
