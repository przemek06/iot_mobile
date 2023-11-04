package edu.pwr.iotmobile.rabbit

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import edu.pwr.iotmobile.error.exception.ChannelException
import edu.pwr.iotmobile.error.exception.QueueException
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.Charset

@Service
@Slf4j
class RabbitListener(
    private val rabbitChannel: RabbitChannel,

    ) {
    val logger: Logger = LoggerFactory.getLogger("RabbitListener")
    val channel = rabbitChannel.createChannel() ?: throw ChannelException()
    //TODO: plan how to handle incoming message
    /**
     * Register new consumer for existing topic
     */
    fun registerConsumer(queueName: String){

        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: AMQP.BasicProperties,
                body: ByteArray
            ) {
                logger.info(consumerTag+": "+body.toString(Charset.defaultCharset()))
            }
        }

        try {
            channel.basicConsume(queueName, true, queueName, consumer)
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    fun cancelConsumer(consumerTag: String){
        channel.basicCancel(consumerTag)
    }
}