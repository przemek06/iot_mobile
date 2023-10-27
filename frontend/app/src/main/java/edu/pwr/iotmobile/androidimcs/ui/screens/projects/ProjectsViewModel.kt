package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // TODO: check error (empty field)

    fun addProject(name: String) {
        viewModelScope.launch {
            // TODO: get user from local db
            val projectDto = ProjectDto(
                name = name,
                createdBy = userRepository.getActiveUserInfo().getOrNull()?.id ?: return@launch
            )
            kotlin.runCatching {
                projectRepository.createProject(projectDto)
            }.onSuccess {
                getProjects()
            }.onFailure {
                toast.toast("Could not create project")
            }
        }
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

    private fun getProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                projectRepository.getProjects()
            }.onSuccess { projects ->
                _uiState.update {
                    it.copy(projects = projects)
                }
            }.onFailure {
                Log.d(TAG, "Get projects error")
            }
        }
    }
}