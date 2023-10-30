package edu.pwr.iotmobile.androidimcs.ui.screens.projects

import edu.pwr.iotmobile.androidimcs.R
import edu.pwr.iotmobile.androidimcs.data.InputFieldData
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto

data class ProjectsUiState(
    val projects: List<ProjectData>,
    val isDialogVisible: Boolean,
    val inputFiled: InputFieldData
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

data class ProjectData(
    val id: Int,
    val name: String,
    val createdBy: Int,
    val connectionKey: String?
) {
    companion object {
        fun ProjectDto.toProjectData(): ProjectData? {
            if (id == null) return null
            return ProjectData(
                id = id,
                name = name,
                createdBy = createdBy,
                connectionKey = connectionKey
            )
        }
    }
}