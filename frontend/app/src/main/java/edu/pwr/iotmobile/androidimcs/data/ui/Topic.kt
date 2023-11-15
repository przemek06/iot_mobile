package edu.pwr.iotmobile.androidimcs.data.ui

import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto

data class Topic(
    val id: Int,
    val title: String,
    val projectId: Int,
    val name: String,
    val currentValue: String? = null,
    val dataType: TopicDataType
) {
    companion object {
        fun TopicDto.toTopic(): Topic? {
            val locId = id ?: return null
            return Topic(
                id = locId,
                projectId = projectId,
                title = name,
                name = uniqueName ?: "",
                dataType = valueType
            )
        }

        fun Topic.toDto() = TopicDto(
            id = id,
            projectId = projectId,
            name = title,
            uniqueName = name,
            valueType = dataType
        )
    }
}