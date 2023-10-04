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
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.OrDivider
import edu.pwr.iotmobile.androidimcs.ui.components.TextLink
import edu.pwr.iotmobile.androidimcs.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(navigation: LoginNavigation) {
    val viewModel = koinViewModel<LoginViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = LoginUiInteraction.default(viewModel)

    LoginScreenContent(
        uiState = uiState,
        uiInteraction = uiInteraction,
        navigation = navigation
    )
}

@Composable
private fun LoginScreenContent(
    uiState: LoginUiState,
    uiInteraction: LoginUiInteraction,
    navigation: LoginNavigation
) {
    // Used to clear focus and hide keyboard when clicked outside input fields
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
        // Title
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Dimensions.space30.HeightSpacer()

        // Input fields
        uiState.inputFields.forEach { inputField ->
            val data = inputField.value
            InputField(
                text = data.text,
                label = stringResource(id = data.label),
                errorText = stringResource(id = data.errorMessage),
                isError = data.isError,
                onValueChange = { uiInteraction.onTextChange(inputField.key, it) }
            )
            Dimensions.space18.HeightSpacer()
        }

        // Forgot your password link
        TextLink(
            text = stringResource(id = R.string.forgot),
            onClick = { navigation.onForgotPassword() }
        )
        Dimensions.space30.HeightSpacer()

        // Login button
        ButtonCommon(
            text = stringResource(id = R.string.login),
            width = 280.dp,
            onClick = { uiInteraction.onLogin() }
        )
        Dimensions.space30.HeightSpacer()

        // Screen divider
        OrDivider()
        Dimensions.space18.HeightSpacer()

        // Register link
        TextLink(
            text = stringResource(id = R.string.create_an_account),
            onClick = { navigation.onCreateAccount() }
        )
    }
}