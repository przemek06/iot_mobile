package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Message
import java.time.LocalDateTime

data class MessageDTO(
    var topic: TopicDTO,
    var message: String,
    var type: String,
    var tsSent: LocalDateTime,
    var id: Int?=null
){
    fun toEntity(): Message{
        return Message(
            topic.toEntity(), message, type, tsSent, id
        )
    }
}
