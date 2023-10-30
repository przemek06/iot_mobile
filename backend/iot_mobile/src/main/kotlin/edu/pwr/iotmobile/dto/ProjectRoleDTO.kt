package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.ProjectRole
import edu.pwr.iotmobile.enums.EProjectRole
import jakarta.validation.constraints.NotNull

data class ProjectRoleDTO(
    val id: Int? = null,
    @field:NotNull
    val projectId: Int,
    @field:NotNull
    val user: UserInfoDTO,
    val role: EProjectRole? = null,
) {
    fun toEntity(role: EProjectRole = EProjectRole.VIEWER) : ProjectRole {
        val project = Project()
        project.id = projectId
        return ProjectRole(project, user.toEntity(), role, id = id)
    }
}
