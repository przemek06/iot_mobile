package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

data class AddComponentUiState(
    val currentPage: AddComponentPage = AddComponentPage.ChooseComponent,
    val bottomNavData: AddComponentViewModel.BottomNavData = AddComponentViewModel.BottomNavData()
)

enum class AddComponentPage {
    ChooseComponent,
    ChooseTopic,
    Settings
}