package edu.pwr.iotmobile.rabbit

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import org.springframework.stereotype.Service

@Service
class RabbitChannel(
    val connection: Connection
) {

    /**
     * Create new channel to attach subscriber
     */
    fun createChannel(): Channel? {
       return connection.createChannel()
    }

    /**
     * Close channel
     * Should be used when connection is broken
     */
    fun closeChannel(channel: Channel){
        channel.close()
    }


}