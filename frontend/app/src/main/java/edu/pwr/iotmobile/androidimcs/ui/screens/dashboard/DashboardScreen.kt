package edu.pwr.iotmobile.androidimcs.ui.screens.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.StringRes
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
import edu.pwr.iotmobile.androidimcs.data.ComponentDetailedType
import edu.pwr.iotmobile.androidimcs.helpers.extensions.firstUppercaseRestLowercase
import edu.pwr.iotmobile.androidimcs.ui.components.ErrorBox
import edu.pwr.iotmobile.androidimcs.ui.components.InfoDialog
import edu.pwr.iotmobile.androidimcs.ui.components.LoadingBox
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardViewModel.Companion.DASHBOARD_DELETED_EVENT
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardViewModel.Companion.TAKE_PICTURE
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
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        viewModel.onTakePhoto(bitmap)
    }

    viewModel.event.CollectEvent(context) {
        when (it) {
            DASHBOARD_DELETED_EVENT -> navigation.onReturn(isDashboardDeleted = true)

            TAKE_PICTURE -> launcher.launch()

            else -> { /*Nothing*/ }
        }
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
    DeleteComponentDialog(
        uiState = uiState,
        uiInteraction = uiInteraction
    )
    InfoComponentDialog(
        uiState = uiState,
        uiInteraction = uiInteraction
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

@Composable
private fun DeleteComponentDialog(
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
) {
    if (uiState.deleteComponentId != null) {
        val component = uiState.components.firstOrNull { it.id == uiState.deleteComponentId }
        SimpleDialog(
            title = stringResource(id = R.string.s81, component?.name ?: ""),
            confirmButtonText = stringResource(id = R.string.yes),
            closeButtonText = stringResource(id = R.string.no),
            onCloseDialog = { uiInteraction.closeDeleteComponentDialog() },
            isLoading = uiState.isDialogLoading,
            onConfirm = { uiInteraction.deleteComponent(uiState.deleteComponentId) }
        ) {
            Text(
                text = stringResource(id = R.string.delete_account_desc2) + ".",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun InfoComponentDialog(
    uiState: DashboardUiState,
    uiInteraction: DashboardUiInteraction,
) {
    if (uiState.infoComponentId != null) {
        val component = uiState.components.firstOrNull { it.id == uiState.infoComponentId }
        InfoDialog(
            title = stringResource(id = R.string.s90),
            onCloseDialog = { uiInteraction.closeInfoComponentDialog() },
        ) {
            ComponentInfoRow(
                value = component?.name,
                description = R.string.name
            )
            ComponentInfoRow(
                value = component?.componentType?.name?.firstUppercaseRestLowercase(),
                description = R.string.s84
            )
            ComponentInfoRow(
                value = component?.detailedType?.name?.firstUppercaseRestLowercase(),
                description = R.string.s85
            )
            ComponentInfoRow(
                value = component?.topic?.name,
                description = R.string.s86
            )
            ComponentInfoRow(
                value = component?.topic?.dataType?.name?.firstUppercaseRestLowercase(),
                description = R.string.s87
            )
            if (component?.detailedType != ComponentDetailedType.Photo) {
                ComponentInfoRow(
                    value = component?.currentValue,
                    description = R.string.s88
                )
            }
            ComponentInfoRow(
                value = component?.currentValueReceivedAt,
                description = R.string.s89
            )
        }
    }
}

@Composable
private fun ComponentInfoRow(
    value: String?,
    @StringRes description: Int
) {
    Text(
        text = stringResource(id = description) + ": $value",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}