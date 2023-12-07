package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ComponentDTO
import edu.pwr.iotmobile.enums.EComponentType
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@DiscriminatorValue("INPUT")
class InputComponent(
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var topic: Topic,
    var onSendValue: String?,
    var onSendAlternative: String?,
    var minValue: String?,
    var maxValue: String?
) : Component() {
    constructor() : this(Topic(), "", "", "", "") {
    }

    fun toDTO(): ComponentDTO {
        return ComponentDTO(
            id,
            name,
            EComponentType.INPUT,
            type,
            size,
            index,
            topic.toDTO(),
            onSendValue = onSendValue,
            onSendAlternative = onSendAlternative,
            minValue = minValue,
            maxValue = maxValue
        )
    }


}