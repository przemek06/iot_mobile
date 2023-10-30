package edu.pwr.iotmobile.rabbit.queue

import edu.pwr.iotmobile.error.exception.QueueException
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint
import org.springframework.stereotype.Service
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer

@Service
@Slf4j
class QueueService(
    val rabbitAdmin: RabbitAdmin,
    val rabbitTemplate: RabbitTemplate,
    val rabbitListenerEndpointRegistry: RabbitListenerEndpointRegistry
) {
    /**
     * Send string message to queue with exchange of the same name
     */
    fun sendMessage(queueName: String, message: String) {
        try {
            log.info(message)
            rabbitTemplate.convertAndSend(queueName, queueName, message)
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    /**
     * Add new queue with binding to exchange of the same name
     */
    fun addQueue(queueName: String) {

        val queue = Queue(queueName, true, false, false)
        val exchange = DirectExchange(queueName)
        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareExchange(exchange)
        val binging = Binding(queueName, Binding.DestinationType.QUEUE, queueName, queueName, null)
        rabbitAdmin.declareBinding(binging)
    }

    /**
     * Force delete rabbit queue
     */
    fun forceDeleteQueue(queueName: String) {
        try {
            rabbitAdmin.deleteQueue(queueName)
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    /**
     * Delete queue if empty and with no listeners
     */
    fun safeDeleteQueue(queueName: String) {
        try {
        rabbitAdmin.deleteQueue(queueName, true, true)
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    /**
     * Delete queue
     */
    fun deleteQueue(queueName: String, force:Boolean){
        if(force){
            forceDeleteQueue(queueName)
        }
        else{
            safeDeleteQueue(queueName)
        }
    }

    /**
     * Remove all messages from queue
     */
    fun clearQueue(queueName: String) {
        try{
        rabbitAdmin.purgeQueue(queueName)
        } catch (_: Exception) {
            throw QueueException()
        }
    }


}