package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.MessageDTO
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Message(
    @ManyToOne
    @JoinColumn(name = "topic_id")
    var topic: Topic,
    @Column(length = 20000)
    var message: String,
    var tsSent: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
) {
    constructor() : this(Topic(), "", LocalDateTime.now().toString())

    fun toDTO(): MessageDTO {
        return MessageDTO(topic.toDTO(), message, "", tsSent, id = id)
    }
}