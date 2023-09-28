@file:OptIn(ExperimentalMaterial3Api::class)

package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun InputField(
    text: String,
    label: String,
    isError: Boolean = false,
    errorText: String = "Error",
    onValueChange: (v: String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        isError = isError,
        supportingText = { ErrorText(errorText, isError) },
        singleLine = true
    )
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