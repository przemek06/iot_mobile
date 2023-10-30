package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.enums.EValueType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

// IMPORTANT - CANNOT UPDATE TOPICS BECAUSE OF UNIQUE NAME, CHANGE IF YOU WANT TO UPDATE
data class TopicDTO(
    val id: Int? = null,
    @field:NotNull
    val projectId: Int,
    @field:NotNull
    @field:Size(max = 64)
    val name: String,
    @field:NotNull
    @field:Size(max = 64)
    val uniqueName: String,
    @field:NotNull
    val valueType: EValueType
) {
    fun toEntity(projectName: String) : Topic {
        val project = Project()
        project.id = projectId
        return Topic(project, name, "$projectName/$uniqueName", valueType, id)
    }

    fun toEntity() : Topic {
        val project = Project()
        project.id = projectId
        return Topic(project, name, uniqueName, valueType, id)
    }

    constructor() : this(1, 1, "default", "default", EValueType.TEXT){}
}
