package edu.pwr.iotmobile.rabbit

import com.rabbitmq.client.AMQP.Channel
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.listener.*
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitAdminConfig() {

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin{
        return RabbitAdmin(connectionFactory)
    }
    @Bean
    fun  rabbitListenerEndpointRegistry():RabbitListenerEndpointRegistry{
        return RabbitListenerEndpointRegistry()
    }
//
//    @Bean
//    fun defaultListenerContainer(){
//        val container: AbstractMessageListenerContainer = SimpleMessageListenerContainer()
//        container.setMessageListener(MessageListenerAdapter())
//
//    }

}