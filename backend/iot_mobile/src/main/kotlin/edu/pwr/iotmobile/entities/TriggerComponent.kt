package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ComponentDTO
import edu.pwr.iotmobile.enums.EComponentType
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne

@Entity
class TriggerComponent(
    @OneToOne
    var eventSource: EventSource,
    @OneToOne
    var actionDestination: ActionDestination,
) : Component() {
    constructor() : this(EventSource(), ActionDestination())

    fun toDTO(): ComponentDTO {
        return ComponentDTO(
            id,
            EComponentType.TRIGGER,
            type,
            size,
            index,
            actionDestinationDTO = actionDestination.toDTO(),
            eventSourceDTO = eventSource.toDTO()
        )
    }
}