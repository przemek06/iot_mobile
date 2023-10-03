package edu.pwr.iotmobile.androidimcs.ui.components

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun Popup(
    title: String,
    buttonText1: String,
    buttonText2: String,
    buttonFunction1: () -> Unit,
    buttonFunction2: () -> Unit,
    content: @Composable() () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
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
                        .fillMaxWidth()
                ) {
                    ButtonCommon(
                        text = buttonText1,
                        onClick = buttonFunction1
                    )
                    ButtonCommon(
                        text = buttonText2,
                        onClick = buttonFunction2,
                        type = ButtonCommonType.Alternative
                    )
                }
            }
        }
    }
}