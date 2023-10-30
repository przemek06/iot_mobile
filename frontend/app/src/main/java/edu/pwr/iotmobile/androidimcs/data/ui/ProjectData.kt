package edu.pwr.iotmobile.androidimcs.data.ui

import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto

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

        fun empty() = ProjectData(
            id = 0,
            name = "",
            createdBy = 0,
            connectionKey = null
        )
    }
}