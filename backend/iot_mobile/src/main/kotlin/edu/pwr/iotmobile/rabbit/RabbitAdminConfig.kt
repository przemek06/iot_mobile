package edu.pwr.iotmobile.rabbit


import com.rabbitmq.client.Connection
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.listener.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class RabbitAdminConfig(

    @Value("\${rabbit.username}")
    val username: String,
    @Value("\${rabbit.password}")
    val password: String,
    @Value("\${rabbit.virtualHost}")
    val virtualHost: String,
    @Value("\${rabbit.host}")
    val host: String,
    @Value("\${rabbit.port}")
    val port: Int
) {


    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }

    @Bean
    fun rabbitListenerEndpointRegistry(): RabbitListenerEndpointRegistry {
        return RabbitListenerEndpointRegistry()
    }

    /**
     * Create rabbit connection for messages exchange
     * Only for subscribers
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun createConnection(): Connection? {
        val factory = com.rabbitmq.client.ConnectionFactory()
        factory.username = username
        factory.password = password
        factory.virtualHost = virtualHost
        factory.host = host
        factory.port = port
        return factory.newConnection()
    }


    fun closeConnection(connection: Connection) {
        connection.close()
    }


}