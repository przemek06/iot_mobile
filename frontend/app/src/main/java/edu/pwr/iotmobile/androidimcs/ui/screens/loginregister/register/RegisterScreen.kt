@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.LoadingBox
import edu.pwr.iotmobile.androidimcs.ui.components.OrDivider
import edu.pwr.iotmobile.androidimcs.ui.components.PasswordInputField
import edu.pwr.iotmobile.androidimcs.ui.components.TextLink
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(navigation: RegisterNavigation) {
    val viewModel = koinViewModel<RegisterViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = RegisterUiInteraction.default(viewModel)

    val context = LocalContext.current
    viewModel.event.CollectEvent(context) {
        uiState.inputFields[RegisterViewModel.InputFieldType.Email]?.text?.let {
            navigation.openAccountInactiveScreen(it)
        }
    }
    viewModel.toast.CollectToast(context)

    LoadingBox(isVisible = uiState.isLoading)

    AnimatedVisibility(
        visible = !uiState.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        RegisterScreenContent(
            uiState = uiState,
            uiInteraction = uiInteraction,
            navigation = navigation
        )
    }
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
    val scrollableState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val bivrList = listOf(
        remember { BringIntoViewRequester() },
        remember { BringIntoViewRequester() },
        remember { BringIntoViewRequester() },
        remember { BringIntoViewRequester() },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                keyboardFocus.clear()
                uiInteraction.checkData()
            }
            .padding(Dimensions.space22)
            .verticalScroll(scrollableState),
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
            val bivr = bivrList[inputField.key.ordinal]
            if (inputField.key == RegisterViewModel.InputFieldType.Password
                || inputField.key == RegisterViewModel.InputFieldType.ConfirmPassword) {
                PasswordInputField(
                    modifier = Modifier
                        .bringIntoViewRequester(bivr)
                        .onFocusEvent {
                            if (it.isFocused) {
                                scope.launch {
                                    bivr.bringIntoView()
                                }
                            }
                        }
                        .focusTarget(),
                    text = data.text,
                    label = stringResource(id = data.label),
                    errorText = stringResource(id = data.errorMessage),
                    isError = data.isError,
                    onValueChange = { uiInteraction.onTextChange(inputField.key, it) })
            } else {
                InputField(
                    modifier = Modifier
                        .bringIntoViewRequester(bivr)
                        .onFocusEvent {
                            if (it.isFocused) {
                                scope.launch {
                                    bivr.bringIntoView()
                                }
                            }
                        }
                        .focusTarget(),
                    text = data.text,
                    label = stringResource(id = data.label),
                    errorText = stringResource(id = data.errorMessage),
                    isError = data.isError,
                    keyboardOptions = data.keyboardOptions,
                    onValueChange = { uiInteraction.onTextChange(inputField.key, it.trim()) }
                )
            }
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