package edu.pwr.iotmobile.dto

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


data class ProjectDTO(
    var id: Int,
    var name: String,
    var createdBy: Int,
    var connectionKey: String,
    var projectRoles: ProjectRoleDTO
)

