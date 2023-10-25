package edu.pwr.iotmobile.androidimcs.data.dto

data class TopicDto(
    val id: Int? = null,
    val projectId: Int,
    val name: String,
    val valueType: String,  // TODO: change
    val isHistoric: Boolean
)
