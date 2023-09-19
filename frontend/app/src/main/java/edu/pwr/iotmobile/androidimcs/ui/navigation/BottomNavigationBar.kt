@file:OptIn(ExperimentalMaterial3Api::class)

package edu.pwr.iotmobile.androidimcs.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    var bottomBarState by rememberSaveable { (mutableStateOf(true)) }
    val bottomNavItems = listOf(
        Screen.Main,
        Screen.Projects,
        Screen.Account,
    )

    // Subscribe to navBackStackEntry, required to get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Log.d("route", currentDestination?.route ?: "no route")

    bottomBarState = when (currentDestination?.route) {
        "flashcard_game/{groupId}" -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarState,
                enter = slideInVertically(initialOffsetY = { s -> s } ),
                exit = slideOutVertically(targetOffsetY = { t -> t } )
            ) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            modifier = Modifier.testTag(screen.tag),
                            icon = {
                                if (screen.icon != null && screen.description != null)
                                    Image(
                                        modifier = Modifier.size(30.dp),
                                        painter = painterResource(id = screen.icon),
                                        contentDescription = stringResource(id = screen.description),
                                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                                    )
                            },
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
        NavGraph(navController = navController, innerPadding = innerPadding)
    }
}
