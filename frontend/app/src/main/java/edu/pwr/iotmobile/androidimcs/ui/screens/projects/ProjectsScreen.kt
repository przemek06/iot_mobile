package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectsScreen(
    navigation: ProjectsNavigation
) {
    val viewModel = koinViewModel<ProjectsViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = ProjectsUiInteraction.default(viewModel)

    val context = LocalContext.current
    viewModel.toast.CollectToast(context)

    ProjectsScreenContent(
        uiState = uiState,
        uiInteraction = uiInteraction,
        navigation = navigation
    )
}

@Composable
fun ProjectsScreenContent(
    uiState: ProjectsUiState,
    uiInteraction: ProjectsUiInteraction,
    navigation: ProjectsNavigation
) {

    if (uiState.isDialogVisible) {
        SimpleDialog(
            title = stringResource(R.string.add_new_project_dialog),
            onCloseDialog = { uiInteraction.setDialogInvisible() },
            onConfirm = {
                uiInteraction.addNewProject(uiState.inputFiled.text)
                uiInteraction.setDialogInvisible()
            }
        ) {
            Text(
                text = stringResource(id = R.string.enter_project_name),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space10.HeightSpacer()
            ProjectsInputField(
                uiState = uiState,
                uiInteraction = uiInteraction
            )
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22)
    ) {
        Dimensions.space40.HeightSpacer()
        Text(
            text = stringResource(id = R.string.projects),
            style = MaterialTheme.typography.titleMedium
        )
        Dimensions.space40.HeightSpacer()
        ButtonCommon(
            text = stringResource(id = R.string.add_new_project),
            type = ButtonCommonType.Secondary
        ) {
            uiInteraction.setDialogVisible()
        }
        Dimensions.space30.HeightSpacer()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimensions.space14),
        ) {
            items(uiState.projects) {
                Block(
                    text = it.name,
                    onClick = { navigation.openProjectDetails(it.id) }
                )
            }
        }
    }
}

@Composable
private fun ProjectsInputField(
    uiState: ProjectsUiState,
    uiInteraction: ProjectsUiInteraction
) {
    InputField(
        text = uiState.inputFiled.text,
        label = stringResource(id = uiState.inputFiled.label),
        onValueChange = { uiInteraction.onTextChange(it) }
    )
}