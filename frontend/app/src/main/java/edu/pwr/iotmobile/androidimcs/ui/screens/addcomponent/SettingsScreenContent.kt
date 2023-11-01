package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun SettingsScreenContent(
    uiState: AddComponentUiState
) {
    Text(
        text = stringResource(id = R.string.s29),
        style = MaterialTheme.typography.titleSmall
    )
    Dimensions.space22.HeightSpacer()
    uiState.settings.forEach {
        Text(
            text = "Label",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space4.HeightSpacer()
        InputField(
            text = "",
            label = "Hello",
            onValueChange = { }
        )
        Dimensions.space26.HeightSpacer()
    }
}