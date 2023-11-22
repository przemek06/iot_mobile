package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ActionDestinationDTO
import edu.pwr.iotmobile.enums.EActionDestinationType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ActionDestination (
    var type: EActionDestinationType,
    var token: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this(EActionDestinationType.NULL, "")

    fun toDTO() : ActionDestinationDTO {
        return ActionDestinationDTO(type, token, id)
    }
}