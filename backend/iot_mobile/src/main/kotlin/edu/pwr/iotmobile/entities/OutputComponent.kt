package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ComponentDTO
import edu.pwr.iotmobile.enums.EComponentType
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
class OutputComponent(
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    var topic: Topic
) : Component() {
    constructor() : this(Topic()) {
    }

    fun toDTO(): ComponentDTO {
        return ComponentDTO(id, EComponentType.OUTPUT, type, size, index, topic.id!!)
    }
}