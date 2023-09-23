package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.Block
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun TopicsScreenContent(
    uiState: ProjectDetailsUiState,
    uiInteraction: ProjectDetailsUiInteraction
) {
    Column {
        ButtonCommon(
            text = "How to access?",
            type = ButtonCommonType.Alternative
        ) {
            Log.d("button", "button pressed")
        }
        Dimensions.space10.HeightSpacer()
        ButtonCommon(
            text = "+ Add new topic",
            type = ButtonCommonType.Secondary
        ) {
            Log.d("button", "button pressed")
        }
        Dimensions.space30.HeightSpacer()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimensions.space14),
        ) {
            items(uiState.topics) {
                ErasableBlock(
                    primaryText = "Topic 1",
                    secondaryText = "Float",
                    onErase = {}
                )
            }
        }
    }
}

@Composable
private fun ErasableBlock(
    primaryText: String,
    secondaryText: String? = null,
    onErase: () -> Unit
) {
    Block(
        text = "Block",
        onClick = {}
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = primaryText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                secondaryText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onErase() },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Erase topic",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}