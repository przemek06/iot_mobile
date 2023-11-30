package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import edu.pwr.iotmobile.androidimcs.data.TopicDataType

data class AddTopicUiState(
    val inputFields: Map<AddTopicViewModel.InputFieldType, AddTopicViewModel.Input>,
    val selectedTopic: TopicDataType?,
    val isLoading: Boolean = false
) {
    companion object {
        fun default(
            inputFields: Map<AddTopicViewModel.InputFieldType, AddTopicViewModel.Input> = emptyMap(),
            selectedTopic: TopicDataType? = null
        ) = AddTopicUiState(
            inputFields= inputFields,
            selectedTopic = selectedTopic
        )
    }
}