package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.MessageDTO
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Message(
    @ManyToOne
    @JoinColumn(name = "topic_id")
    var topic: Topic,
    var message: String,
    var tsSent: LocalDateTime,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
) {
    constructor() : this(Topic(), "", LocalDateTime.now())

    fun toDTO(): MessageDTO {
        return MessageDTO(topic.toDTO(), message, "", tsSent, id = id)
    }
}