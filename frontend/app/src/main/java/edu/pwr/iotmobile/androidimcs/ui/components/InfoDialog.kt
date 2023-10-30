package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun InfoDialog(
    title: String,
    @StringRes buttonText: Int = R.string.close,
    buttonFunction: () -> Unit,
    content: @Composable() () -> Unit = {}
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Card(
            modifier = Modifier
                .padding(Dimensions.space14)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Dimensions.space14)
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
                    onClick = buttonFunction,
                    type = ButtonCommonType.Primary
                )
            }
        }
    }
}