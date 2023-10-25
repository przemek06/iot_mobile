package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData

data class ProjectsUiState(
    val projects: List<Any>,
    val isDialogVisible: Boolean,
    val inputFiled: InputFieldData
) {
    companion object {
        fun default(
            projects: List<Any> = emptyList(),
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