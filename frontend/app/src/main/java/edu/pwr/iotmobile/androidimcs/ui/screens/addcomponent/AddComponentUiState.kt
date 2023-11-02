package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import android.adservices.topics.Topic

data class AddComponentUiState(
    val currentPage: AddComponentPage = AddComponentPage.ChooseComponent,
    val bottomNavData: AddComponentViewModel.BottomNavData = AddComponentViewModel.BottomNavData(),
    val inputComponents: List<AddComponentViewModel.ComponentChoiceData> = emptyList(),
    val topics: List<Topic> = emptyList(),
    val settings: Map<AddComponentViewModel.SettingType, AddComponentViewModel.SettingData> = emptyMap(),
    val newComponent: AddComponentViewModel.ComponentData = AddComponentViewModel.ComponentData()
)

enum class AddComponentPage {
    ChooseComponent,
    ChooseTopic,
    Settings
}