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
@DiscriminatorValue("OUTPUT")
class OutputComponent(
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var topic: Topic,
) : Component() {
    constructor() : this(Topic()) {
    }

    fun toDTO(): ComponentDTO {
        return ComponentDTO(id, EComponentType.OUTPUT, type, size, index, topic.id!!)
    }
}