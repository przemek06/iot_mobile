package edu.pwr.iotmobile.rabbit

import com.rabbitmq.client.*
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import java.io.IOException

@Slf4j
class RabbitConsumer {

    /**
     * consumerTag should be project name combined with random number
     */
    fun registerConsumer(channel: Channel, queueName: String, consumerTag: String){
        val autoAck: Boolean = false
        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: AMQP.BasicProperties,
                body: ByteArray
            ) {
                val routingKey: String = envelope.routingKey
                val contentType: String = properties.contentType
                val deliveryTag: Long = envelope.deliveryTag
                channel.basicAck(deliveryTag, false)
                log.info(body)
            }
        }
        channel.basicConsume(queueName, autoAck, consumerTag,consumer)
    }

    fun cancelConsumer(channel: Channel, consumerTag: String){
        channel.basicCancel(consumerTag)
    }

    fun returnMessage(channel: Channel){
        channel.addReturnListener { _, _, _, _, _, _ -> }
    }

}