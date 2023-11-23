package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.RadioButtonWithText

@Composable
fun AdditionalScreenContent(
    uiState: AddComponentUiState,
    uiInteraction: AddComponentUiInteraction,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        // Discord channels
        if (uiState.discordChannels.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.s51),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            itemsIndexed(uiState.discordChannels) { index, item ->
                RadioButtonWithText(
                    text = item.title,
                    isSelected = item.isChecked,
                    onClick = { uiInteraction.onChooseDiscordChannel(index) }
                )
            }
        }
    }
}