package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import edu.pwr.iotmobile.androidimcs.data.ComponentDetailedType
import edu.pwr.iotmobile.androidimcs.data.ui.Topic

data class AddComponentUiState(
    val currentPage: AddComponentPage = AddComponentPage.ChooseComponent,
    val bottomNavData: AddComponentViewModel.BottomNavData = AddComponentViewModel.BottomNavData(),
    val inputComponents: List<AddComponentViewModel.ComponentChoiceData> = emptyList(),
    val outputComponents: List<AddComponentViewModel.ComponentChoiceData> = emptyList(),
    val triggerComponents: List<AddComponentViewModel.ComponentChoiceData> = emptyList(),
    val topics: List<Topic> = emptyList(),
    val settings: Map<AddComponentViewModel.SettingType, AddComponentViewModel.SettingData> = emptyMap(),
    val chosenComponentType: ComponentDetailedType? = null,
    val chosenTopic: Topic? = null,
    val discordChannels: List<AddComponentViewModel.DiscordChannel> = emptyList(),
    val discordUrl: String? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false
)

enum class AddComponentPage {
    ChooseComponent,
    ChooseTopic,
    Settings,
    Additional
}