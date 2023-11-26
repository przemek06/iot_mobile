package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.Message
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class MessageDTO(
    @field:NotNull
    val topic: TopicDTO = TopicDTO(),
    @field:NotNull
    @field:Size(max = 1024)
    val message: String,
    @field:NotNull
    val connectionKey: String,
    val tsSent: String = LocalDateTime.now().toString(),
    val id: Int?=null
){

    fun toEntity(): Message{
        return Message(
            topic.toEntity(), message, tsSent, id
        )
    }
}
