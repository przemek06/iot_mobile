package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Project
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ProjectDTO(
    val id: Int?=null,
    @field:NotNull
    @field:Size(min = 3, max = 64)
    val name: String,
    val createdBy: UserInfoDTO,
    val connectionKey: String?,
) {
    fun toEntity() : Project {
        return Project(
            name, createdBy.toEntity(), connectionKey ?: "", id
        )
    }
}
