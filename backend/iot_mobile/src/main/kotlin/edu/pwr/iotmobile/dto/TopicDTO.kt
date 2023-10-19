package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.Topic

data class TopicDTO(
    val id: Int? = null,
    val projectId: Int,
    val name: String,
    val valueType: String,
    val isHistoric: Boolean
) {
    fun toEntity() : Topic {
        val project = Project()
        project.id = projectId
        return Topic(project, name, valueType, isHistoric, id)
    }
}
