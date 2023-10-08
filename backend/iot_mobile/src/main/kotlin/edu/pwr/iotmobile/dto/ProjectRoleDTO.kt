package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Project

data class ProjectRoleDTO(
    val id: Int,
    val project: ProjectDTO,
    val user: UserDTO,
    val role: String
)
