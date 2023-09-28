package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.WidthSpacer

@Composable
fun Option(
    text: String,
    isBold: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.optionItemHeight)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
        ) {
            Text(
                modifier = Modifier.weight(weight = 1f, fill = true),
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space10.WidthSpacer()
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart),
            thickness = Dimensions.space2,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}