package edu.pwr.iotmobile.rabbit

import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitAdminConfig() {

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin{
        return RabbitAdmin(connectionFactory)
    }
}