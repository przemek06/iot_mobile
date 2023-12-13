package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionInputField(
    text: String,
    label: String,
    isError: Boolean = false,
    errorText: String = "Error",
    onValueChange: (v: String) -> Unit
) {
    InputField(
        modifier = Modifier.height(160.dp),
        text = text,
        label = label,
        isError = isError,
        isSingleLine = false,
        errorText = errorText,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        keyboardActions = KeyboardActions.Default
    )
}