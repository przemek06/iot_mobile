@file:OptIn(ExperimentalComposeUiApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommon
import edu.pwr.iotmobile.androidimcs.ui.components.ButtonCommonType
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.components.OrDivider
import edu.pwr.iotmobile.androidimcs.ui.components.TopBar
import edu.pwr.iotmobile.androidimcs.ui.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(navigation: ForgotPasswordNavigation) {
    val viewModel = koinViewModel<ForgotPasswordViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiInteraction = ForgotPasswordUiInteraction.default(viewModel)

    ForgotPasswordScreenContent(
        uiState = uiState,
        uiInteraction = uiInteraction,
        navigation = navigation
    )
}

@Composable
private fun ForgotPasswordScreenContent(
    uiState: ForgotPasswordUiState,
    uiInteraction: ForgotPasswordUiInteraction,
    navigation: ForgotPasswordNavigation
) {
    // Used to clear focus and hide keyboard when clicked outside input fields
    val focusManager = LocalFocusManager.current
    val keyboardFocus = KeyboardFocusController(
        keyboardController = LocalSoftwareKeyboardController.current,
        focusManager = focusManager
    )

    Column {
        TopBar(
            padding = Dimensions.space22,
            onReturn = { navigation.onReturn() }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    keyboardFocus.clear()
                }
                .padding(horizontal = Dimensions.space40),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.forgot),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
            Dimensions.space30.HeightSpacer()
            if (uiState.isInputCode) {
                EnterCodeContent(uiState, uiInteraction)
            } else {
                EnterEmailContent(uiState, uiInteraction)
            }
        }
    }
}

@Composable
private fun EnterEmailContent(
    uiState: ForgotPasswordUiState,
    uiInteraction: ForgotPasswordUiInteraction
) {
    val inputField = uiState.inputFields[ForgotPasswordViewModel.InputFieldType.Email]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.s1),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Dimensions.space18.HeightSpacer()
        inputField?.let {
            InputField(
                text = it.text,
                label = stringResource(id = it.label),
                onValueChange = { uiInteraction.onTextChange(
                    type = ForgotPasswordViewModel.InputFieldType.Email,
                    text = it
                ) }
            )
        }
        Dimensions.space40.HeightSpacer()
        ButtonCommon(
            text = stringResource(id = R.string.confirm),
            onClick = { uiInteraction.onConfirmEmail() }
        )
    }
}


// TODO: squish on smaller devices
@Composable
private fun EnterCodeContent(
    uiState: ForgotPasswordUiState,
    uiInteraction: ForgotPasswordUiInteraction
) {
    val codeInputField = uiState.inputFields[ForgotPasswordViewModel.InputFieldType.Code]
    val newPasswordInputField = uiState.inputFields[ForgotPasswordViewModel.InputFieldType.NewPassword]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.s2),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Dimensions.space10.HeightSpacer()
        codeInputField?.let {
            InputField(
                text = it.text,
                label = stringResource(id = it.label),
                onValueChange = { uiInteraction.onTextChange(
                    type = ForgotPasswordViewModel.InputFieldType.Code,
                    text = it
                ) }
            )
        }
        Dimensions.space30.HeightSpacer()
        Text(
            text = stringResource(id = R.string.s3),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Dimensions.space10.HeightSpacer()
        newPasswordInputField?.let {
            InputField(
                text = it.text,
                label = stringResource(id = it.label),
                onValueChange = { uiInteraction.onTextChange(
                    type = ForgotPasswordViewModel.InputFieldType.NewPassword,
                    text = it
                ) }
            )
        }
        Dimensions.space30.HeightSpacer()
        ButtonCommon(
            text = stringResource(id = R.string.confirm),
            onClick = { uiInteraction.onConfirmNewPassword() }
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
            text = stringResource(id = R.string.confirm),
            type = ButtonCommonType.Alternative,
            onClick = { uiInteraction.onResendCode() }
        )
    }
}