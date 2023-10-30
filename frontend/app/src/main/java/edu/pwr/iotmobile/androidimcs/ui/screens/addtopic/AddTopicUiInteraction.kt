package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import edu.pwr.iotmobile.androidimcs.data.TopicDataType

interface AddTopicUiInteraction {

    fun onTextChange(text: String, type: AddTopicViewModel.InputFieldType)
    fun selectTopic(topic: TopicDataType)
    fun addTopic(projectId: Int?)

    companion object {
        fun default(
            viewModel: AddTopicViewModel
        ) = object : AddTopicUiInteraction {
            override fun onTextChange(text: String,  type: AddTopicViewModel.InputFieldType) {
                viewModel.onTextChange(type, text)
            }

            override fun selectTopic(topic: TopicDataType) {
                viewModel.selectTopic(topic)
            }

            override fun addTopic(projectId: Int?) {
                viewModel.addTopic(projectId)
            }
        }
    }
}