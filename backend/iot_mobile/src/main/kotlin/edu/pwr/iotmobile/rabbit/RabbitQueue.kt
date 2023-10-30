package edu.pwr.iotmobile.rabbit

import com.rabbitmq.client.Channel
import edu.pwr.iotmobile.entities.Message
import org.springframework.stereotype.Service

@Service
class RabbitQueue {

    /**
     * queue declare arguments
     * queueName - self explanatory
     * durable - recover on node boot, persist messages
     * exclusive - can only be consumed by own connection
     * autoDelete - delete after last consumer is canceled
     * arguments - queue properties
     */
    fun addQueue(channel: Channel, exchangeName: String, queueName: String, routingKey: String = ""){
        channel.exchangeDeclare(exchangeName, "direct", true)
        channel.queueDeclare(queueName, true, false, false, mapOf())
        channel.queueBind(queueName, exchangeName, routingKey)
    }

    fun deleteQueue(channel: Channel, queueName: String){
        channel.queueDelete(queueName)
    }

    fun deleteAllMessages(channel: Channel, queueName: String){
        channel.queuePurge(queueName)
    }

    fun publishMessage(channel: Channel, queueName: String, exchangeName: String, message: Message){
        val messageBody = message.message?.toByteArray()
        channel.basicPublish(exchangeName, queueName, null, messageBody)
    }


}