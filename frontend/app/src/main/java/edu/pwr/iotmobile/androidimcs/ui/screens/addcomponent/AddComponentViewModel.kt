package edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val BottomNavDataList = listOf(
    AddComponentViewModel.BottomNavData(),
    AddComponentViewModel.BottomNavData(hasPrevButton = true),
    AddComponentViewModel.BottomNavData(
        nextButtonText = R.string.confirm,
        hasPrevButton = true
    )
)

class AddComponentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddComponentUiState())
    val uiState = _uiState.asStateFlow()

    fun navigateNext() {
        when (_uiState.value.currentPage) {
            AddComponentPage.ChooseComponent ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.ChooseTopic,
                    bottomNavData = getBottomNavData(AddComponentPage.ChooseTopic)
                ) }
            AddComponentPage.ChooseTopic ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.Settings,
                    bottomNavData = getBottomNavData(AddComponentPage.Settings)
                ) }
            AddComponentPage.Settings ->
                { /*TODO: add new component*/ }
        }
    }

    fun navigatePrevious() {
        when (_uiState.value.currentPage) {
            AddComponentPage.ChooseTopic ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.ChooseComponent,
                    bottomNavData = getBottomNavData(AddComponentPage.ChooseComponent)
                ) }
            AddComponentPage.Settings ->
                _uiState.update { it.copy(
                    currentPage = AddComponentPage.ChooseTopic,
                    bottomNavData = getBottomNavData(AddComponentPage.ChooseTopic)
                ) }
            else -> { /*Do nothing*/ }
        }
    }

    private fun getBottomNavData(page: AddComponentPage) = when (page) {
        AddComponentPage.ChooseComponent -> BottomNavDataList[0]
        AddComponentPage.ChooseTopic -> BottomNavDataList[1]
        AddComponentPage.Settings -> BottomNavDataList[2]
    }

    data class BottomNavData(
        @StringRes val nextButtonText: Int = R.string.next,
        val hasPrevButton: Boolean = false,
    )

    data class ComponentData(
        val title: String,
        @DrawableRes val iconRes: Int
    )
}