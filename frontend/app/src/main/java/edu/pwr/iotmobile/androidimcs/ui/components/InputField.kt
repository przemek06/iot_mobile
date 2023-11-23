@file:OptIn(ExperimentalMaterial3Api::class)

package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import edu.pwr.iotmobile.androidimcs.extensions.conditional

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    width: Dp? = null,
    isSingleLine: Boolean = true,
    isError: Boolean = false,
    errorText: String = "Error",
    trailingIcon: @Composable () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (v: String) -> Unit
) {
    Column(
        modifier = Modifier.conditional(width != null) {
            width?.let { width(it) } ?: run { this }
        }
    ) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = text,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            singleLine = isSingleLine,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError
        )
        ErrorText(
            text = errorText,
            isError = isError
        )
    }
}

@Composable
private fun ErrorText(
    text: String,
    isError: Boolean
) {
    if (isError) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.error
        )
    }
}