package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ComponentDTO
import edu.pwr.iotmobile.enums.EComponentType
import jakarta.persistence.CascadeType
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne

@Entity
@DiscriminatorValue("TRIGGER")
class TriggerComponent(
    @OneToOne(cascade = [CascadeType.ALL])
    var eventSource: EventSource,
    @OneToOne(cascade = [CascadeType.ALL])
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