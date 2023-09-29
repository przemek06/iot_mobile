package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun TextLink(
    text: String,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier.clickable { onClick() },
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        textDecoration = TextDecoration.Underline,
        color = MaterialTheme.colorScheme.onBackground
    )
}