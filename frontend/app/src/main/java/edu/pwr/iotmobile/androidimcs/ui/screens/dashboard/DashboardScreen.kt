package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ErrorBox
import edu.pwr.iotmobile.androidimcs.ui.components.LoadingBox
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(navigation: DashboardNavigation) {
    val viewModel = koinViewModel<DashboardViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = DashboardUiInteraction.default(viewModel, navigation::openAddComponentScreen)

    LaunchedEffect(Unit) {
        navigation.dashboardId?.let {
            viewModel.init(it, navigation.projectId, navigation.dashboardName ?: "")
        }
    }

    val context = LocalContext.current
    viewModel.event.CollectEvent(context) {
        navigation.onReturn()
    }
    viewModel.toast.CollectToast(context)

    ErrorBox(
        isVisible = uiState.isError,
        onReturn = navigation::onReturn
    )
    LoadingBox(isVisible = uiState.isLoading)

    AnimatedVisibility(
        visible = !uiState.isError && !uiState.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        DashboardScreenContent(
            uiState =  uiState,
            uiInteraction = uiInteraction,
            navigation = navigation
        )
    }
}

@Composable
private fun DashboardScreenContent(
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
    navigation: DashboardNavigation
) {
    DeleteDashboardDialog(
        uiState = uiState,
        uiInteraction = uiInteraction,
        navigation = navigation
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            text = navigation.dashboardName,
            menuItems = uiState.menuOptionsList,
            onReturn = navigation::onReturn
        )
        ComponentsList(
            uiState =  uiState,
            uiInteraction = uiInteraction
        )
    }
}

@Composable
private fun DeleteDashboardDialog(
    navigation: DashboardNavigation,
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
) {
    if (uiState.isDeleteDashboardDialogVisible) {
        SimpleDialog(
            title = stringResource(id = R.string.s64, navigation.dashboardName ?: ""),
            confirmButtonText = stringResource(id = R.string.yes),
            closeButtonText = stringResource(id = R.string.no),
            onCloseDialog = { uiInteraction.toggleDeleteDashboardDialog() },
            isLoading = uiState.isDialogLoading,
            onConfirm = { uiInteraction.deleteDashboard() }
        ) {
            Text(
                text = stringResource(id = R.string.s65),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space10
            Text(
                text = stringResource(id = R.string.delete_account_desc2) + ".",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}