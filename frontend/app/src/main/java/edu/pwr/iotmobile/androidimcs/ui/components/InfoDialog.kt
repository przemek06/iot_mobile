package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun InfoDialog(
    title: String,
    @StringRes buttonText: Int = R.string.close,
    onCloseDialog: () -> Unit,
    content: @Composable() () -> Unit = {}
) {
    AppDialog {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Dimensions.space22.HeightSpacer()

            content()

            Dimensions.space22.HeightSpacer()

            ButtonCommon(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = buttonText),
                onClick = onCloseDialog,
                type = ButtonCommonType.Alternative
            )
        }
    }
}