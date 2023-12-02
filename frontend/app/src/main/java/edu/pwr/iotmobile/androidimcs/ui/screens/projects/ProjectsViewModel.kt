package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult
import edu.pwr.iotmobile.androidimcs.data.ui.ProjectData.Companion.toProjectData
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ProjectsVM"

class ProjectsViewModel(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    val toast: Toast
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState.default())
    val uiState = _uiState.asStateFlow()

    init {
        getProjects()
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(inputFiled = it.inputFiled.copy(text = text))
        }
    }

    fun addProject(name: String) {
        if (name.isBlank()) {
            _uiState.update {
                it.copy(inputFiled = it.inputFiled.copy(
                    isError = true,
                    errorMessage = R.string.s59
                ))
            }
            return
        }
        setDialogIsLoading(true)
        viewModelScope.launch {
            try {
                val projectDto = ProjectDto(
                    name = name,
                    createdBy = userRepository.getLoggedInUser().firstOrNull()?.id ?: return@launch
                )
                val result = projectRepository.createProject(projectDto)
                when (result) {
                    CreateResult.Success -> {
                        closeDialog()
                        getProjects()
                    }
                    CreateResult.AlreadyExists -> {
                        _uiState.update {
                            it.copy(inputFiled = it.inputFiled.copy(
                                isError = true,
                                errorMessage = R.string.s60
                            ))
                        }
                    }
                    else -> {
                        toast.toast("Could not create project.")
                        setDialogIsLoading(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("ProjectList", "Error while creating new project.", e)
                setDialogIsLoading(false)
            }
        }
    }

    fun openDialog() {
        _uiState.update {
            it.copy(isDialogVisible = true)
        }
    }
    fun closeDialog() {
        _uiState.update {
            it.copy(
                isDialogVisible = false,
                isError = false,
                isDialogLoading = false,
                inputFiled = it.inputFiled.copy(
                    text = "",
                    isError = false
                )
            )
        }
    }

    private fun getProjects() {
        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                projectRepository.getProjects()
            }.onSuccess { projects ->
                _uiState.update { ui ->
                    ui.copy(
                        projects = projects.mapNotNull { it.toProjectData() },
                        inputFiled = ui.inputFiled.copy(text = ""),
                        isLoading = false,
                        isError = false
                    )
                }
                return@launch
            }.onFailure {
                Log.d(TAG, "Get projects error")
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    private fun setIsLoading(value: Boolean) {
        _uiState.update { ui ->
            ui.copy(isLoading = value)
        }
    }

    private fun setDialogIsLoading(value: Boolean) {
        _uiState.update { ui ->
            ui.copy(isDialogLoading = value)
        }
    }
}