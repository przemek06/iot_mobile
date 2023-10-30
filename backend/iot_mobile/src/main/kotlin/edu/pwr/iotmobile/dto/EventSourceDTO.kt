package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.EventSource

data class EventSourceDTO(
    val type: String,
    val token: String,
    val id: Int?
) {
    fun toEntity() : EventSource {
        return EventSource(type, token, id)
    }
}
