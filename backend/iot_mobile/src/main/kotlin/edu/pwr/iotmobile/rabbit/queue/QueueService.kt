package edu.pwr.iotmobile.rabbit.queue

import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.error.exception.QueueException
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
@Slf4j
class QueueService(
    val rabbitAdmin: RabbitAdmin,
    val rabbitTemplate: RabbitTemplate
) {
    /**
     * Send string message to queue with exchange of the same name
     */
    fun sendMessage(exchangeName: String, message: String, connectionKey: String) {
        try {
            log.info(message)
            rabbitTemplate.convertAndSend(exchangeName, connectionKey, message)
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    /**
     * Add new queue with binding to exchange of the same name
     */
    fun addExchange(exchangeName: String) {

//        val queue = Queue(queueName, true, false, false)
        val exchange = DirectExchange(exchangeName)
//        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareExchange(exchange)
//        val binding = Binding(queueName, Binding.DestinationType.QUEUE, queueName, queueName, null)
//        rabbitAdmin.declareBinding(binding)
    }

    fun addQueue(exchangeName: String, connectionKey: String) : String {
        val queue = Queue("", true, false, false)
        val queueName = rabbitAdmin.declareQueue(queue) ?: throw InvalidStateException()
//        val queue = rabbitAdmin.declareQueue() ?: throw InvalidStateException()
        println("queueName = $queue")
        val binding = Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, connectionKey, null)
        rabbitAdmin.declareBinding(binding)
        return queueName
    }

    fun deleteQueue(queueName: String) {
        rabbitAdmin.deleteQueue(queueName)
    }

    /**
     * Force delete rabbit queue
     */
    fun forceDeleteExchange(exchangeName: String) {
        try {
//            rabbitAdmin.deleteQueue(queueName)
            rabbitAdmin.deleteExchange(exchangeName)
        } catch (_: Exception) {
            throw QueueException()
        }
    }
}