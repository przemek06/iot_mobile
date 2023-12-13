@file:OptIn(ExperimentalComposeUiApi::class)

package edu.pwr.iotmobile.androidimcs.helpers

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController

class KeyboardFocusController(
    private val keyboardController: SoftwareKeyboardController?,
    private val focusManager: FocusManager
) {
    fun clear() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }
}