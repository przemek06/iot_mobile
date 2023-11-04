package edu.pwr.iotmobile.androidimcs.data.dto

data class ComponentDataDto(
    val id: Int? = null,
    val componentType: String,
    val type: String,
    val size: Int,
    val index: Int = 0,
    val topicId: Int? = null,
    val name: String? = null,
    val defaultValue: Any? = null,
    val onSendValue: Any? = null,
    val onSendAlternativeValue: Any? = null,
    val maxValue: Any? = null,
    val minValue: Any? = null,
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