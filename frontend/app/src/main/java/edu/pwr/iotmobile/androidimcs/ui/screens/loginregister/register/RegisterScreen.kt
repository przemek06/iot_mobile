@file:OptIn(ExperimentalComposeUiApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.OrDivider
import edu.pwr.iotmobile.androidimcs.ui.components.TextLink
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(navigation: RegisterNavigation) {
    val viewModel = koinViewModel<RegisterViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = RegisterUiInteraction.default(viewModel)

    val context = LocalContext.current
    viewModel.event.CollectEvent(context) {
        // TODO: navigate
    }
    viewModel.toast.CollectToast(context)

    RegisterScreenContent(
        uiState = uiState,
        uiInteraction = uiInteraction,
        navigation = navigation
    )
}

@Composable
private fun RegisterScreenContent(
    uiState: RegisterUiState,
    uiInteraction: RegisterUiInteraction,
    navigation: RegisterNavigation
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
                uiInteraction.checkData()
            }
            .padding(Dimensions.space22),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = stringResource(id = R.string.register),
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
        Dimensions.space14.HeightSpacer()

        // Register button
        ButtonCommon(
            text = stringResource(id = R.string.register),
            width = 280.dp,
            onClick = { uiInteraction.onRegister() }
        )
        Dimensions.space30.HeightSpacer()

        // Screen divider
        OrDivider()
        Dimensions.space18.HeightSpacer()

        // Login link
        TextLink(
            text = stringResource(id = R.string.login),
            onClick = { navigation.onLogin() }
        )
    }
}