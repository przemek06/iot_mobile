@file:OptIn(ExperimentalComposeUiApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun DashboardsScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
    navigation: ProjectDetailsNavigation
) {
    val focusManager = LocalFocusManager.current
    val keyboardFocus = KeyboardFocusController(
        keyboardController = LocalSoftwareKeyboardController.current,
        focusManager = focusManager
    )

    AddNewDashboardDialog(
        uiState = uiState,
        uiInteraction = uiInteraction,
        onCloseDialog = {
            keyboardFocus.clear()
            uiInteraction.toggleAddDashboardDialog()
        }
    )
    LazyColumn {
        item {
            Dimensions.space22.HeightSpacer()
        }

        if (uiState.userProjectRole != UserProjectRole.VIEWER) {
            item {
                ButtonCommon(
                    text = stringResource(id = R.string.add_new_dashboard),
                    type = ButtonCommonType.Secondary
                ) { uiInteraction.toggleAddDashboardDialog() }
                Dimensions.space30.HeightSpacer()
            }
        }

        items(uiState.dashboards) {
            Block(
                text = it.name,
                onClick = { navigation.openDashboardScreen(it.id, it.name) }
            )
            Dimensions.space14.HeightSpacer()
        }
    }
}

@Composable
private fun AddNewDashboardDialog(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
    onCloseDialog: () -> Unit
) {
    if (uiState.isAddDialogVisible) {
        SimpleDialog(
            title = stringResource(R.string.add_new_dashboard_dialog),
            isLoading = uiState.isDialogLoading,
            onCloseDialog = { onCloseDialog() },
            onConfirm = {
                uiInteraction.addNewDashboard(uiState.inputFieldDashboard.text)
            }
        ) {
            Text(
                text = stringResource(id = R.string.enter_dashboard_name_below),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space10.HeightSpacer()
            InputFieldDashboard(
                uiState = uiState,
                uiInteraction = uiInteraction
            )
        }
    }
}

@Composable
private fun InputFieldDashboard(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    InputField(
        text = uiState.inputFieldDashboard.text,
        label = stringResource(id = uiState.inputFieldDashboard.label),
        isError = uiState.inputFieldDashboard.isError,
        errorText = stringResource(id = uiState.inputFieldDashboard.errorMessage),
        onValueChange = { uiInteraction.onTextChangeDashboard(it) }
    )
}