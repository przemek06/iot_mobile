package edu.pwr.iotmobile.androidimcs.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier = composed {
    if (condition)
        then(modifier(Modifier))
    else this
}