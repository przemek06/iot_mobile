package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Message
import java.time.LocalDateTime

data class MessageDTO(
    var topic: TopicDTO? = TopicDTO(),
    var message: String,
    var type: String,
    var tsSent: LocalDateTime?=LocalDateTime.now(),
    var id: Int?=null
){
    //TODO: project name to be added here
    fun toEntity(): Message{
        return Message(
            topic!!.toEntity(), message, type, tsSent, id
        )
    }
}
