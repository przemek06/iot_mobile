package edu.pwr.iotmobile.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.error.exception.ChannelException
import edu.pwr.iotmobile.error.exception.QueueException
import edu.pwr.iotmobile.service.IncomingMessageService
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException

@Service
@Slf4j
class RabbitListener(
    private val rabbitChannel: RabbitChannel,
    private val incomingMessageService: IncomingMessageService

) {
    val logger: Logger = LoggerFactory.getLogger("RabbitListener")
    val channel = rabbitChannel.createChannel() ?: throw ChannelException()
    val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    /**
     * Register new consumer for existing topic
     */
    fun registerConsumer(queueNames: List<String> ){
        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: AMQP.BasicProperties,
                body: ByteArray
            ) {
                val messageDTO: MessageDTO = objectMapper.readValue(body, MessageDTO::class.java)
                logger.info("$consumerTag: $messageDTO")
                incomingMessageService.processEntityChange(messageDTO)
            }
        }

        try {
            for (queueName:String in queueNames){
                channel.basicConsume(queueName, true, queueName, consumer)
            }
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    fun cancelConsumer(consumerTag: String){
        channel.basicCancel(consumerTag)
    }
}