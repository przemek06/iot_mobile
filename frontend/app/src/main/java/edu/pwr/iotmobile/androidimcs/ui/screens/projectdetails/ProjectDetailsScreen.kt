package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.MenuItem
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

private val TABS = listOf(
    R.string.dashboards,
    R.string.topics,
    R.string.group
)

@Composable
fun ProjectDetailsScreen(
    navigation: ProjectDetailsNavigation
) {
    Log.d("nav", "project id:")
    navigation.projectId?.let { Log.d("nav", it) }
    val viewModel = koinViewModel<ProjectDetailsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ProjectDetailsScreenContent(
        uiState = uiState,
        uiInteraction = ProjectDetailsUiInteraction.default(viewModel)
    )
}

@Composable
fun ProjectDetailsScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22)
    ) {
        TopBar(
            MenuItem(title = stringResource(id = R.string.delete_project), onClick = {}),
            onReturn = { /*TODO*/ }
        )
        Dimensions.space10.HeightSpacer()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Project 1",
                style = MaterialTheme.typography.titleSmall
            )
            ButtonCommon(
                text = stringResource(id = R.string.show_access),
                type = ButtonCommonType.Alternative
            ) {
                Log.d("button", "button pressed")
            }
        }
        Dimensions.space10.HeightSpacer()
        TabRow(selectedTabIndex = uiState.selectedTabIndex) {
            TABS.forEachIndexed { index, title ->
                Tab(text = { Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                ) },
                    selected = uiState.selectedTabIndex == index,
                    onClick = { uiInteraction.setSelectedTabIndex(index) }
                )
            }
        }
        Dimensions.space22.HeightSpacer()

        TabContent(uiState = uiState, uiInteraction = uiInteraction)
    }
}

@Composable
private fun TabContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibilityTabContainer(visible = uiState.selectedTabIndex == 0) {
            DashboardsScreenContent(uiState, uiInteraction)
        }
        AnimatedVisibilityTabContainer(visible = uiState.selectedTabIndex == 1) {
            TopicsScreenContent(uiState, uiInteraction)
        }
        AnimatedVisibilityTabContainer(visible = uiState.selectedTabIndex == 2) {
            GroupScreenContent(uiState, uiInteraction)
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