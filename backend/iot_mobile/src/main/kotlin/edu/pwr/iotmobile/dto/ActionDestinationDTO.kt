package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.ActionDestination
import edu.pwr.iotmobile.enums.EActionDestinationType

data class ActionDestinationDTO(
    val type: EActionDestinationType,
    val token: String,
    val id: Int?
) {
    fun toEntity() : ActionDestination {
        return ActionDestination(type, token, id)
    }
}
