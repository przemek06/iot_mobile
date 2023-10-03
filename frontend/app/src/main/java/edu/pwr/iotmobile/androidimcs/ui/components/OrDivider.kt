package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions

@Composable
fun OrDivider() {
    Row(
        modifier = Modifier.width(300.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomDivider()
        Text(
            modifier = Modifier.padding(horizontal = Dimensions.space22),
            text = stringResource(id = R.string.or),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        CustomDivider()
    }
}

@Composable
private fun RowScope.CustomDivider() {
    Divider(
        modifier = Modifier.weight(weight = 1f, fill = true),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onBackground
    )
}