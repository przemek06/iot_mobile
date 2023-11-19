package edu.pwr.iotmobile.androidimcs.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

fun Modifier.clickableWithoutIndication(
    onClick: () -> Unit
): Modifier = this.clickable(
    interactionSource = MutableInteractionSource(),
    indication = null
) { onClick() }
