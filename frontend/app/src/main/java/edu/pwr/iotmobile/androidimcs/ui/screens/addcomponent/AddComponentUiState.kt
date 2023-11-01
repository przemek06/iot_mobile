package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

data class AddComponentUiState(
    val currentPage: AddComponentPage = AddComponentPage.ChooseComponent,
    val bottomNavData: AddComponentViewModel.BottomNavData = AddComponentViewModel.BottomNavData(),
    val inputComponents: List<Any> = listOf(1,2,3),
    val topics: List<Any> = listOf(1,2,3),
)

enum class AddComponentPage {
    ChooseComponent,
    ChooseTopic,
    Settings
}