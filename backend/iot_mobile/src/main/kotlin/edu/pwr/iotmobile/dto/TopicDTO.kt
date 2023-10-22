package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.enums.EValueType

data class TopicDTO(
    val id: Int? = null,
    val projectId: Int,
    val name: String,
    val valueType: EValueType,
    val isHistoric: Boolean
) {
    fun toEntity() : Topic {
        val project = Project()
        project.id = projectId
        return Topic(project, name, valueType, isHistoric, id)
    }
}
