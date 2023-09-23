package edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProjectDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectDetailsUiState.default())
    val uiState = _uiState.asStateFlow()

    fun setSelectedTabIndex(index: Int) {
        _uiState.update {
            it.copy(selectedTabIndex = index)
        }
    }
}