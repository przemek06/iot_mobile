package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.extensions.conditional

@Composable
fun LoadingBox(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = true
) {
    Box(modifier = modifier.conditional(isFullScreen) { fillMaxSize() }) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp).align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}