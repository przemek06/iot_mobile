package edu.pwr.iotmobile.rabbit.message

import edu.pwr.iotmobile.entities.Message
import edu.pwr.iotmobile.error.exception.QueueException
import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
@Slf4j
class MessageService (
    val rabbitTemplate: RabbitTemplate,
){

    /**
     * Send message to specified queue
     */
    //TODO: deserialize whole entity
    fun sendMessage(message: Message){
        try{
        rabbitTemplate.convertAndSend(message.topic.name, message.topic.name, message.message)
        } catch (_: Exception) {
            throw QueueException()
        }
    }



}