package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
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
fun ProjectDetailsScreen() {
    val viewModel = koinViewModel<ProjectDetailsViewModel>()
    val uiState = ProjectDetailsUiState.default(
        dashboards = listOf(1,2,3),
        topics = listOf(1,2,3)
    )
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
        Dimensions.space40.HeightSpacer()
        Row(
            modifier = Modifier.fillMaxWidth()
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
        when (uiState.selectedTabIndex) {
            0 -> DashboardsScreenContent(uiState, uiInteraction)
            1 -> TopicsScreenContent(uiState, uiInteraction)
            2 -> GroupScreenContent(uiState, uiInteraction)
        }
        Dimensions.space40.HeightSpacer()
    }

}