package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions

@Composable
fun AppDialog(
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Card {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(
                        horizontal = Dimensions.space22,
                        vertical = Dimensions.space26
                    )
            ) {
                content()
            }
        }
    }
}