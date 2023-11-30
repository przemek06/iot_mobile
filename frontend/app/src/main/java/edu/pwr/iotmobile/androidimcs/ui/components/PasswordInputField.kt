package edu.pwr.iotmobile.androidimcs.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import edu.pwr.iotmobile.androidimcs.R

@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    width: Dp? = null,
    isError: Boolean = false,
    errorText: String = "Error",
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (v: String) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    val visualTransformation =
        if (isVisible) VisualTransformation.None
        else PasswordVisualTransformation()

    InputField(
        modifier = modifier,
        text = text,
        label = label,
        width = width,
        isError = isError,
        errorText = errorText,
        onValueChange = onValueChange,
        trailingIcon = {
            PasswordIconButton(
                isVisible = isVisible,
                onClick = { isVisible = !isVisible }
            )
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}

@Composable
private fun PasswordIconButton(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    val icon =
        if (isVisible) R.drawable.ic_eye_closed
        else R.drawable.ic_eye
    val description =
        if (isVisible) R.string.s19
        else R.string.s18

    IconButton(
        onClick = onClick
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = description),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
    }

}