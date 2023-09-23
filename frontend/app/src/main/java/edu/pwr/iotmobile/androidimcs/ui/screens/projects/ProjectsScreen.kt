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
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun ProjectsScreen() {
    val uiState = ProjectsUiState.default(projects = listOf(1,2,3))
    ProjectsScreenContent(uiState)
}

@Composable
fun ProjectsScreenContent(
    uiState: ProjectsUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.space22)
    ) {
        Dimensions.space40.HeightSpacer()
        Text(
            text = "Projects",
            style = MaterialTheme.typography.titleMedium
        )
        Dimensions.space40.HeightSpacer()
        ButtonCommon(
            text = "+ Add new project",
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
                    onClick = {}
                )
            }
        }
    }
}