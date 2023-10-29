package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer

@Composable
fun SimpleDialog(
    title: String,
    buttonText1: String = stringResource(id = R.string.cancel),
    buttonText2: String = stringResource(id = R.string.confirm),
    onCloseDialog: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable() () -> Unit = {}
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Dimensions.space22.HeightSpacer()

                content()

                Dimensions.space22.HeightSpacer()

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonCommon(
                        modifier = Modifier.weight(1f),
                        text = buttonText1,
                        onClick = onCloseDialog,
                        type = ButtonCommonType.Alternative
                    )
                    Dimensions.space22.WidthSpacer()
                    ButtonCommon(
                        modifier = Modifier.weight(1f),
                        text = buttonText2,
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}