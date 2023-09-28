package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions

/**
 * A block list item, used commonly in various lists.
 */
@Composable
fun Block(
    text: String = "",
    onClick: () -> Unit,
    content: @Composable () -> Unit = { DefaultContent(text) }
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraSmall,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(
            width = Dimensions.space2,
            color = MaterialTheme.colorScheme.primary
        ) ,
        contentPadding = PaddingValues(horizontal = Dimensions.space22, vertical = Dimensions.space14),
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    ) {
        content()
    }
}

@Composable
private fun DefaultContent(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}