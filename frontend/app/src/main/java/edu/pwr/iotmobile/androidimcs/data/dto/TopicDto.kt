package edu.pwr.iotmobile.androidimcs.data.dto

import edu.pwr.iotmobile.androidimcs.data.TopicDataType

data class TopicDto(
    val id: Int? = null,
    val projectId: Int,
    val name: String,
    val uniqueName: String,
    val valueType: TopicDataType
)
