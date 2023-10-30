package edu.pwr.iotmobile.rabbit

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class RabbitChannel(
    private val connection: Connection
) {

    fun createChannel(): Channel? {
        return connection.createChannel()
    }

    fun closeChannel(channel: Channel){
        channel.close()
    }

}