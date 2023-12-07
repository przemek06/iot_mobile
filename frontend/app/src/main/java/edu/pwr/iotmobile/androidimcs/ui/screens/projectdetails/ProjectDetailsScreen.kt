package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.UserProjectRole
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.ErrorBox
import edu.pwr.iotmobile.androidimcs.ui.components.InfoDialog
import edu.pwr.iotmobile.androidimcs.ui.components.LoadingBox
import edu.pwr.iotmobile.androidimcs.ui.components.SimpleDialog
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectDetailsScreen(
    navigation: ProjectDetailsNavigation
) {
    val viewModel = koinViewModel<ProjectDetailsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(navigation)
    }

    LaunchedEffect(navigation.isTopicSuccess) {
        viewModel.updateTopics()
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
        ProjectDetailsScreenContent(
            uiState = uiState,
            uiInteraction = ProjectDetailsUiInteraction.default(viewModel),
            navigation = navigation
        )
    }
}

@Composable
private fun ProjectDetailsScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
    navigation: ProjectDetailsNavigation
) {
    var isAccessDialogVisible by remember { mutableStateOf(false) }
    ShowAccessDialog(
        isVisible = isAccessDialogVisible,
        connectionString = uiState.projectData.connectionKey ?: "",
        uiState = uiState,
        uiInteraction =  uiInteraction,
        onCloseDialog = { isAccessDialogVisible = false }
    )
    DeleteProjectDialog(
        uiState = uiState,
        uiInteraction = uiInteraction
    )
    LeaveProjectDialog(
        uiState = uiState,
        uiInteraction = uiInteraction
    )

    Column {
        TopBar(
            menuItems = uiState.menuOptionsList,
            onReturn = { navigation.onReturn() }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimensions.space22)
        ) {
            Dimensions.space10.HeightSpacer()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.projectData.name,
                    style = MaterialTheme.typography.titleSmall
                )
                if (uiState.userProjectRole != UserProjectRole.VIEWER) {
                    ButtonCommon(
                        text = stringResource(id = R.string.show_access),
                        type = ButtonCommonType.Alternative
                    ) {
                        isAccessDialogVisible = true
                    }
                }
            }
            Dimensions.space10.HeightSpacer()
            TabRow(selectedTabIndex = uiState.selectedTabIndex) {
                ProjectDetailsViewModel.ProjectTab.values().forEach { tab ->
                    Tab(text = {
                        Text(
                            text = stringResource(id = tab.labelId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                        selected = uiState.selectedTabIndex == tab.index,
                        onClick = { uiInteraction.setSelectedTabIndex(tab) }
                    )
                }
            }

            TabContent(uiState, uiInteraction, navigation)
        }
    }
}

@Composable
private fun TabContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
    navigation: ProjectDetailsNavigation
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibilityTabContainer(visible = uiState.selectedTabIndex == 0) {
            DashboardsScreenContent(uiState, uiInteraction, navigation)
        }
        AnimatedVisibilityTabContainer(visible = uiState.selectedTabIndex == 1) {
            TopicsScreenContent(uiState, uiInteraction, navigation)
        }
        AnimatedVisibilityTabContainer(visible = uiState.selectedTabIndex == 2) {
            GroupScreenContent(uiState)
        }
    }
}

@Composable
private fun AnimatedVisibilityTabContainer(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        content()
    }
}

@Composable
private fun DeleteProjectDialog(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
) {
    if (uiState.isDeleteProjectDialogVisible) {
        SimpleDialog(
            title = stringResource(id = R.string.s61, uiState.projectData.name),
            confirmButtonText = stringResource(id = R.string.yes),
            closeButtonText = stringResource(id = R.string.no),
            onCloseDialog = { uiInteraction.toggleDeleteProjectDialog() },
            isLoading = uiState.isDialogLoading,
            onConfirm = { uiInteraction.deleteProject() }
        ) {
            Text(
                text = stringResource(id = R.string.delete_account_desc),
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
private fun LeaveProjectDialog(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
) {
    if (uiState.isLeaveProjectDialogVisible) {
        SimpleDialog(
            title = stringResource(id = R.string.s113, uiState.projectData.name),
            confirmButtonText = stringResource(id = R.string.yes),
            closeButtonText = stringResource(id = R.string.no),
            onCloseDialog = { uiInteraction.toggleLeaveProjectDialog() },
            isLoading = uiState.isDialogLoading,
            onConfirm = { uiInteraction.leaveGroup() }
        ) {
            Text(
                text = stringResource(id = R.string.s114),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun ShowAccessDialog(
    isVisible: Boolean,
    connectionString: String,
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction,
    onCloseDialog: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    if (isVisible) {
        InfoDialog(
            title = stringResource(id = R.string.s45),
            onCloseDialog = onCloseDialog
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CopyText(
                    text = stringResource(id = R.string.s44) + " " + connectionString,
                    onCopyClick = {
                        clipboardManager.setText(AnnotatedString(text = connectionString))
                    }
                )
                if (uiState.userProjectRole == UserProjectRole.ADMIN) {
                    Dimensions.space10.HeightSpacer()
                    Box(modifier = Modifier.fillMaxWidth()) {
                        ButtonCommon(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(id = R.string.s46),
                            onClick = uiInteraction::regenerateConnectionKey
                        )
                    }
                }
                Dimensions.space22.HeightSpacer()
                Text(
                    text = stringResource(id = R.string.s48),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun CopyText(
    text: String,
    onCopyClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = Dimensions.space4),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(onClick = onCopyClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_copy),
                contentDescription = "Copy",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}