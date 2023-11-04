package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import edu.pwr.iotmobile.androidimcs.data.ui.Topic

interface AddComponentUiInteraction {
    fun navigateNext()
    fun navigatePrevious()
    fun onChooseComponent(componentData: AddComponentViewModel.ComponentChoiceData)
    fun onChooseTopic(topic: Topic)
    fun onTextChange(
        type: AddComponentViewModel.SettingType,
        text: String
    )

    companion object {
        fun default(
            viewModel: AddComponentViewModel
        ) = object : AddComponentUiInteraction {
            override fun navigateNext() {
                viewModel.navigateNext()
            }

            override fun navigatePrevious() {
                viewModel.navigatePrevious()
            }

            override fun onChooseComponent(componentData: AddComponentViewModel.ComponentChoiceData) {
                viewModel.onChooseComponent(componentData)
            }

            override fun onChooseTopic(topic: Topic) {
                viewModel.onChooseTopic(topic)
            }

            override fun onTextChange(type: AddComponentViewModel.SettingType, text: String) {
                viewModel.onTextChange(type, text)
            }

        }

        fun empty() = object : AddComponentUiInteraction {
            override fun navigateNext() {}
            override fun navigatePrevious() {}
            override fun onChooseComponent(componentData: AddComponentViewModel.ComponentChoiceData) {}
            override fun onChooseTopic(topic: Topic) {}
            override fun onTextChange(type: AddComponentViewModel.SettingType, text: String) {}

        }
    }
}