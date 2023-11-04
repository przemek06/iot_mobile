package edu.pwr.iotmobile.androidimcs.data.ui

import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto

data class Topic(
    val id: Int,
    val title: String,
    val name: String,
    val dataType: TopicDataType
) {
    companion object {
        fun TopicDto.toTopic(): Topic? {
            val locId = id ?: return null
            return Topic(
                id = locId,
                title = name,
                name = uniqueName ?: "", // TODO: will be not null
                dataType = valueType
            )
        }
    }
}