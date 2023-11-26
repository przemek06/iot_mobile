package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun SimpleDialog(
    title: String,
    closeButtonText: String = stringResource(id = R.string.cancel),
    confirmButtonText: String = stringResource(id = R.string.confirm),
    onCloseDialog: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    AppDialog {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Dimensions.space10.HeightSpacer()

        content()

        Dimensions.space22.HeightSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ButtonCommon(
                text = closeButtonText,
                onClick = onCloseDialog,
                isDisabled = isLoading,
                type = ButtonCommonType.Alternative
            )
            ButtonCommon(
                text = confirmButtonText,
                onClick = onConfirm
            )
        }
    }
}