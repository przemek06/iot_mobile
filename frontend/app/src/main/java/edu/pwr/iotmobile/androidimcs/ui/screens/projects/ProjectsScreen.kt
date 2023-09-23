package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun ProjectsScreen(
    navigation: ProjectsNavigation
) {
    val uiState = ProjectsUiState.default(projects = listOf(1,2,3))
    ProjectsScreenContent(
        uiState = uiState,
        navigation = navigation
    )
}

@Composable
fun ProjectsScreenContent(
    uiState: ProjectsUiState,
    navigation: ProjectsNavigation
) {
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
            Log.d("button", "button pressed")
        }
        Dimensions.space30.HeightSpacer()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimensions.space14),
        ) {
            items(uiState.projects) {
                Block(
                    text = "Block",
                    onClick = { navigation.openProjectDetails("1") }
                )
            }
        }
    }
}