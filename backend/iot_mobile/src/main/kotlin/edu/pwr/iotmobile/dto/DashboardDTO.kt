package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Dashboard
import edu.pwr.iotmobile.entities.Project
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class DashboardDTO(
    val id: Int?=null,
    @field:NotNull
    @field:Size(min = 3, max = 64)
    val name: String,
    @field:NotNull
    val projectId: Int
    ) {
    fun toEntity() : Dashboard {
        val project = Project()
        project.id = projectId
        return Dashboard(project, name, id)
    }
}
