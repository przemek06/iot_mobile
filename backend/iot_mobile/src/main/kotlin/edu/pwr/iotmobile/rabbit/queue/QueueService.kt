package edu.pwr.iotmobile.rabbit.queue

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Queue
import org.springframework.stereotype.Service
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate

@Service
class QueueService(
    val rabbitAdmin: RabbitAdmin,
    val rabbitTemplate: RabbitTemplate
) {
    /**
     * send message to default DIRECT exchange using explicit
     * queueName and message value
     */
    fun sendMessage(queueName: String, message: String){
        rabbitTemplate.convertAndSend("", queueName, message)
    }

    /**
     * add new queue with binding to default exchange
     */
    fun addQueue(queueName: String){
        val queue = Queue(queueName, true, false, false)
        val binging = Binding(queueName, Binding.DestinationType.QUEUE, "", queueName, null)
        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareBinding(binging)
    }


}