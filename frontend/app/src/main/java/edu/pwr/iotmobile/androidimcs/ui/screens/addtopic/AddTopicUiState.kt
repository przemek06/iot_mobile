package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class AddTopicUiState(
    val inputFieldData: InputFieldData,
    val selectedTopic: String
) {
    companion object {
        fun default(
            inputFieldData: InputFieldData = InputFieldData(
                text = "",
                label = R.string.name
            ),
            selectedTopic: String = ""
        ) = AddTopicUiState(
            inputFieldData = inputFieldData,
            selectedTopic = selectedTopic
        )
    }
}