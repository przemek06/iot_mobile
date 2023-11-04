package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import edu.pwr.iotmobile.androidimcs.extensions.conditional
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions

enum class ButtonCommonType {
    Primary,
    Secondary,
    Alternative
}

@Composable
fun ButtonCommon(
    modifier: Modifier = Modifier,
    text: String = "",
    type: ButtonCommonType = ButtonCommonType.Primary,
    width: Dp? = null,
    onClick: () -> Unit
) {
    val textColor =
        if (type == ButtonCommonType.Alternative) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.onSecondary
    val colors = when (type) {
        ButtonCommonType.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
        ButtonCommonType.Secondary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        )
        ButtonCommonType.Alternative -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
        )
    }
    val border = if (type == ButtonCommonType.Alternative) BorderStroke(
        width = Dimensions.space2,
        color = MaterialTheme.colorScheme.primary
    ) else null

    Button(
        modifier = modifier
            .conditional(width != null) { width?.let { widthIn(min = it) } ?: run { modifier } },
        shape = MaterialTheme.shapes.medium,
        colors = colors,
        border = border,
        contentPadding = PaddingValues(horizontal = Dimensions.space22, vertical = Dimensions.space14),
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = textColor,
            maxLines = 1
        )
    }
}