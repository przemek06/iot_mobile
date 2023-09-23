package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun DashboardsScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    Column {
        ButtonCommon(
            text = stringResource(id = R.string.add_new_dashboard),
            type = ButtonCommonType.Secondary
        ) {
            Log.d("button", "button pressed")
        }
        Dimensions.space30.HeightSpacer()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimensions.space14),
        ) {
            items(uiState.dashboards) {
                Block(
                    text = "Block",
                    onClick = {}
                )
            }
        }
    }
}