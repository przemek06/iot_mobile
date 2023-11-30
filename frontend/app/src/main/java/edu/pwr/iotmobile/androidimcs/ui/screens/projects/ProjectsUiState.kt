package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.ui.ProjectData

data class ProjectsUiState(
    val projects: List<ProjectData>,
    val isDialogVisible: Boolean,
    val inputFiled: InputFieldData,
    val isLoading: Boolean = true,
    val isDialogLoading: Boolean = false,
    val isError: Boolean = false
) {
    companion object {
        fun default(
            projects: List<ProjectData> = emptyList(),
            isDialogVisible: Boolean = false,
            inputFiled: InputFieldData = InputFieldData(
                text = "",
                label = R.string.name
            )
        ) = ProjectsUiState(
            projects = projects,
            isDialogVisible = isDialogVisible,
            inputFiled = inputFiled
        )
    }
}