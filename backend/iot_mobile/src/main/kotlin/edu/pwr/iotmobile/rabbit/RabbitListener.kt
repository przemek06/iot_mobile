package edu.pwr.iotmobile.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.error.exception.ChannelException
import edu.pwr.iotmobile.rabbit.queue.QueueService
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.IOException

@Service
@Slf4j
class RabbitListener(
    private val rabbitChannel: RabbitChannel,
    private val queueService: QueueService

) {
    val logger: Logger = LoggerFactory.getLogger("RabbitListener")
    val channel = rabbitChannel.createChannel() ?: throw ChannelException()
    val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(KotlinModule.Builder().build())

    /**
     * Register new consumer for existing topic
     */
    fun registerConsumer(exchangeName: String, connectionKey: String): Pair<String, Flux<MessageDTO>> {
        val sink = Sinks.many().unicast().onBackpressureBuffer<MessageDTO>()

        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: AMQP.BasicProperties,
                body: ByteArray
            ) {
                val messageDTO: MessageDTO = objectMapper.readValue(String(body), MessageDTO::class.java)
                logger.info("$consumerTag: $messageDTO")
                val result = sink.tryEmitNext(messageDTO)
                if (!result.isSuccess) {
                    logger.error("Could not emit the next value from the Rabbit Listener")
                }
            }
        }

        val queueName = queueService.addQueue(exchangeName, connectionKey)
        channel.basicConsume(queueName, true, queueName, consumer)
        return queueName to sink.asFlux()
    }

    fun cancelConsumer(consumerTag: String) {
        channel.basicCancel(consumerTag)
        queueService.deleteQueue(consumerTag)
        logger.info("Consumer with tag $consumerTag canceled.")
    }
}