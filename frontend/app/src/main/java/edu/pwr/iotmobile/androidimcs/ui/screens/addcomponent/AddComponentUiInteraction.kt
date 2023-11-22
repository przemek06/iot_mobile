package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import org.koin.core.scope.ScopeID

interface AddComponentUiInteraction {
    fun navigateNext(scopeID: ScopeID?)
    fun navigatePrevious()
    fun onChooseComponent(componentData: AddComponentViewModel.ComponentChoiceData)
    fun onChooseTopic(topic: Topic)
    fun onTextChange(
        type: AddComponentViewModel.SettingType,
        text: String
    )
    fun onChooseDiscordChannel(index: Int)

    companion object {
        fun default(
            viewModel: AddComponentViewModel
        ) = object : AddComponentUiInteraction {
            override fun navigateNext(scopeID: ScopeID?) {
                scopeID?.let {
                    viewModel.navigateNext(scopeID)
                }
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

            override fun onChooseDiscordChannel(index: Int) {
                viewModel.onChooseDiscordChannel(index)
            }

        }

        fun empty() = object : AddComponentUiInteraction {
            override fun navigateNext(scopeID: ScopeID?) {}
            override fun navigatePrevious() {}
            override fun onChooseComponent(componentData: AddComponentViewModel.ComponentChoiceData) {}
            override fun onChooseTopic(topic: Topic) {}
            override fun onTextChange(type: AddComponentViewModel.SettingType, text: String) {}
            override fun onChooseDiscordChannel(index: Int) {}
        }
    }
}