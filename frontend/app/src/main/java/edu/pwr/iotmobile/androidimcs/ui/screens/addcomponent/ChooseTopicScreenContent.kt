package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.RadioButtonWithText
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun ChooseTopicScreenContent(
    uiState: AddComponentUiState
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.s27),
            style = MaterialTheme.typography.titleSmall
        )
        Dimensions.space22.HeightSpacer()
        ButtonCommon(
            text = stringResource(id = R.string.add_new_topic),
            type = ButtonCommonType.Secondary,
            onClick = {}
        )
        Dimensions.space22.HeightSpacer()
        uiState.topics.forEach {
            RadioButtonWithText(
                text = "Speed",
                isSelected = false,
                onClick = { },
            )
        }
    }
}