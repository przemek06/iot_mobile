package edu.pwr.iotmobile.androidimcs.data.dto

data class ComponentDto(
    val id: Int? = null,
    val componentType: String,
    val type: String,
    val size: Int, // 1 or 2
    val index: Int = 0,
    val topic: TopicDto? = null,
    val name: String? = null,
    val onSendValue: String? = null,
    val onSendAlternativeValue: String? = null,
    val maxValue: String? = null,
    val minValue: String? = null,
    val actionDestinationDTO: ActionDestinationDTO? = null,
    val eventSourceDTO: EventSourceDTO? = null
)

data class ActionDestinationDTO(
    val id: Int?,
    val type: String,
    val token: String,
)

data class EventSourceDTO(
    val id: Int?,
    val type: String,
    val token: String,
)