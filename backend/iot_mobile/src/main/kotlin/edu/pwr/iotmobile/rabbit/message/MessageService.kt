package edu.pwr.iotmobile.rabbit.message

import edu.pwr.iotmobile.entities.Message
import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
@Slf4j
class MessageService (
    val rabbitTemplate: RabbitTemplate,
){

    /**
     * send message to default DIRECT exchange using Message entity
     */
    fun sendMessage(message: Message){
        rabbitTemplate.convertAndSend("", message.topic.name, message.message)
    }



}