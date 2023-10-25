package edu.pwr.iotmobile.androidimcs.ui.screens.addtopic

import edu.pwr.iotmobile.androidimcs.data.TopicType

interface AddTopicUiInteraction {

    fun onTextChange(text: String)
    fun selectTopic(topic: TopicType)

    companion object {
        fun default(viewModel: AddTopicViewModel) = object : AddTopicUiInteraction {
            override fun onTextChange(text: String) {
                viewModel.onTextChange(text = text)
            }

            override fun selectTopic(topic: TopicType) {
                viewModel.selectTopic(topic)
            }
        }
    }
}