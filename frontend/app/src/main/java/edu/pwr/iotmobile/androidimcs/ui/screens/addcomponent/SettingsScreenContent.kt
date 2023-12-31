@file:OptIn(ExperimentalComposeUiApi::class)

package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.helpers.KeyboardFocusController
import edu.pwr.iotmobile.androidimcs.ui.components.DescriptionInputField
import edu.pwr.iotmobile.androidimcs.ui.components.InputField
import edu.pwr.iotmobile.androidimcs.ui.theme.Dimensions
import edu.pwr.iotmobile.androidimcs.ui.theme.HeightSpacer

@Composable
fun SettingsScreenContent(
    uiState: AddComponentUiState,
    uiInteraction: AddComponentUiInteraction
) {
    val focusManager = LocalFocusManager.current
    val keyboardFocus = KeyboardFocusController(
        keyboardController = LocalSoftwareKeyboardController.current,
        focusManager = focusManager
    )
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                keyboardFocus.clear()
                uiInteraction.checkInputFields()
            }
    ) {
        item {
            Text(
                text = stringResource(id = R.string.s29),
                style = MaterialTheme.typography.titleSmall
            )
            Dimensions.space22.HeightSpacer()
        }

        uiState.settings.forEach {
            item {
                Text(
                    text = stringResource(id = it.value.title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Dimensions.space4.HeightSpacer()

                it.value.description?.let {
                    Text(
                        text = stringResource(id = it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Dimensions.space4.HeightSpacer()
                }

                it.value.linkText?.let { linkText ->
                    Text(
                        modifier = Modifier.clickable {
                            it.value.link?.let {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                context.startActivity(intent)
                            }
                        },
                        text = stringResource(id = linkText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                    Dimensions.space4.HeightSpacer()
                }

                if (it.value.isDescription) {
                    DescriptionInputField(
                        text = it.value.inputFieldData.text,
                        label = stringResource(id = it.value.inputFieldData.label),
                        isError = it.value.inputFieldData.isError,
                        errorText = stringResource(id = it.value.inputFieldData.errorMessage),
                        onValueChange = { text -> uiInteraction.onTextChange(it.key, text) }
                    )
                } else {
                    InputField(
                        text = it.value.inputFieldData.text,
                        label = stringResource(id = it.value.inputFieldData.label),
                        isError = it.value.inputFieldData.isError,
                        errorText = stringResource(id = it.value.inputFieldData.errorMessage),
                        onValueChange = { text -> uiInteraction.onTextChange(it.key, text) },
                        keyboardOptions = it.value.inputFieldData.keyboardOptions
                    )
                }
                Dimensions.space26.HeightSpacer()
            }
        }
    }
}