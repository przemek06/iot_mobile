@file:OptIn(ExperimentalComposeUiApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen() {
    val viewModel = koinViewModel<LoginViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = LoginUiInteraction.default(viewModel)

    LoginScreenContent(
        uiState = uiState,
        uiInteraction = uiInteraction
    )
}

@Composable
private fun LoginScreenContent(
    uiState: LoginUiState,
    uiInteraction: LoginUiInteraction
) {
    val focusManager = LocalFocusManager.current
    val keyboardFocus = KeyboardFocusController(
        keyboardController = LocalSoftwareKeyboardController.current,
        focusManager = focusManager
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                keyboardFocus.clear()
            }
            .padding(Dimensions.space22),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space40.HeightSpacer()
        uiState.inputFields.forEachIndexed { index, inputField ->
            InputField(
                text = inputField.text,
                label = stringResource(id = inputField.label),
                errorText = stringResource(id = inputField.errorMessage),
                isError = inputField.isError,
                onValueChange = { uiInteraction.onTextChange(inputField, it) }
            )
            if (index < uiState.inputFields.lastIndex)
                Dimensions.space18.HeightSpacer()
        }
    }
}