package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ComponentDTO
import edu.pwr.iotmobile.enums.EComponentType
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@DiscriminatorValue("TRIGGER")
class TriggerComponent(
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var topic: Topic,
    @OneToOne(cascade = [CascadeType.ALL])
    var actionDestination: ActionDestination,
    @Column(length = 5000)
    var pattern: String
) : Component() {
    constructor() : this(Topic(), ActionDestination(), "")

    fun toDTO(): ComponentDTO {
        return ComponentDTO(
            id,
            name,
            EComponentType.TRIGGER,
            type,
            size,
            index,
            actionDestinationDTO = actionDestination.toDTO(),
            topic = topic.toDTO(),
            pattern = pattern
        )
    }
}