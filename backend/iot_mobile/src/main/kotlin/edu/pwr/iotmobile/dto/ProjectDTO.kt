package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.User
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ProjectDTO(
    val id: Int?=null,
    @field:NotNull
    @field:Size(max = 64)
    val name: String,
    @field:NotNull
    val createdBy: Int,
    val connectionKey: String?,
) {
    fun toEntity() : Project {
        val user = User()
        user.id = createdBy
        return Project(
            name, user, connectionKey ?: "", id
        )
    }
}
