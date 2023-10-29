package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ActionDestinationDTO
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ActionDestination (
    var type: String,
    var token: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this("", "")

    fun toDTO() : ActionDestinationDTO {
        return ActionDestinationDTO(type, token, id)
    }
}