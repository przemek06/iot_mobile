package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(navigation: DashboardNavigation) {
    val viewModel = koinViewModel<DashboardViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = DashboardUiInteraction.default(viewModel, navigation::openAddComponentScreen)

    LaunchedEffect(Unit) {
        navigation.dashboardId?.let {
            viewModel.init(it)
        }
    }

    DashboardScreenContent(
        uiState =  uiState,
        uiInteraction = uiInteraction,
        navigation = navigation
    )
}

@Composable
private fun DashboardScreenContent(
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
    navigation: DashboardNavigation
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            menuItems = uiState.menuOptionsList,
            onReturn = navigation::onReturn
        )
        ComponentsList(
            uiState =  uiState,
            uiInteraction = uiInteraction,
            navigation = navigation
        )
    }
}