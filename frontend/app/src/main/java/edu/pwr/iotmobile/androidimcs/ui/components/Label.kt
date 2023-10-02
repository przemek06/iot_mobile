package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.LightPurple

@Composable
fun Label(text: String) {
    Box(modifier = Modifier.clip(CircleShape).background(color = LightPurple)) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = Dimensions.space8, vertical = Dimensions.space2),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}