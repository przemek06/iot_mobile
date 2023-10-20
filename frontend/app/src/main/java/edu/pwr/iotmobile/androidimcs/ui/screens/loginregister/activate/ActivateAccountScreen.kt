@file:OptIn(ExperimentalComposeUiApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.ActivateAccountType
import edu.pwr.iotmobile.androidimcs.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.OrDivider
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActivateAccountScreen(navigation: ActivateAccountNavigation) {
    val viewModel = koinViewModel<ActivateAccountViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = ActivateAccountUiInteraction.default(viewModel)

    val context = LocalContext.current
    viewModel.event.CollectEvent(context) {
        navigation.onReturn()
    }
    viewModel.toast.CollectToast(context)

    if (uiState.isAccountActivated) {
        ActivateAccountSuccessScreenContent()
    } else {
        ActivateAccountScreenContent(
            uiState = uiState,
            uiInteraction = uiInteraction,
            navigation = navigation
        )
    }
}

@Composable
private fun ActivateAccountSuccessScreenContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.s14),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
            Dimensions.space10.HeightSpacer()
            Text(
                text = stringResource(id = R.string.s15),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
            Dimensions.space22.HeightSpacer()
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
private fun ActivateAccountScreenContent(
    uiState: ActivateAccountUiState,
    uiInteraction: ActivateAccountUiInteraction,
    navigation: ActivateAccountNavigation
) {
    // Used to clear focus and hide keyboard when clicked outside input fields
    val focusManager = LocalFocusManager.current
    val keyboardFocus = KeyboardFocusController(
        keyboardController = LocalSoftwareKeyboardController.current,
        focusManager = focusManager
    )

    val titleId =
        if (navigation.type == ActivateAccountType.AfterLogin) R.string.s7
        else R.string.s8

    Box(modifier = Modifier.fillMaxSize()) {
        TopBar(
            modifier = Modifier.align(Alignment.TopStart),
            padding = Dimensions.space22,
            onReturn = { navigation.onReturn() }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    keyboardFocus.clear()
                }
                .padding(horizontal = Dimensions.space40)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
            Dimensions.space30.HeightSpacer()

            Text(
                text = stringResource(id = R.string.s9),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Dimensions.space10.HeightSpacer()

            // Code input field
            InputField(
                text = uiState.inputField.text,
                label = stringResource(id = uiState.inputField.label),
                onValueChange = { uiInteraction.onTextChange(text = it) }
            )
            Dimensions.space30.HeightSpacer()

            // Button to activate
            ButtonCommon(
                text = stringResource(id = R.string.activate),
                width = Dimensions.buttonWidth,
                onClick = { uiInteraction.onActivate() }
            )
            Dimensions.space30.HeightSpacer()
            OrDivider()
            Dimensions.space30.HeightSpacer()
            Text(
                text = stringResource(id = R.string.s13),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Dimensions.space18.HeightSpacer()
            ButtonCommon(
                text = stringResource(id = R.string.resend_link),
                type = ButtonCommonType.Alternative,
                width = Dimensions.buttonWidth,
                onClick = { uiInteraction.onResendCode() }
            )
        }
    }
}