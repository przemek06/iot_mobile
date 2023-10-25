package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import androidx.lifecycle.ViewModel
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProjectsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(projects = listOf("1", "1", "1"))
        }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputFiled = it.inputFiled.copy(text = text))
        }
    }
    fun addProject(name: String) {

    }
    fun setDialogVisible() {
        _uiState.update {
            it.copy(isDialogVisible = true)
        }
    }
    fun setDialogInvisible() {
        _uiState.update {
            it.copy(isDialogVisible = false)
        }
    }
}