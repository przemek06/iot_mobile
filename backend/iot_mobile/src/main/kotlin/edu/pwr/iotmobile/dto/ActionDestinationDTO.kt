package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.ActionDestination

data class ActionDestinationDTO(
    val type: String,
    val token: String,
    val id: Int?
) {
    fun toEntity() : ActionDestination {
        return ActionDestination(type, token, id)
    }
}
