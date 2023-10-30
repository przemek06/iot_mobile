package edu.pwr.iotmobile.rabbit
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.event.EventListener

@Configuration
class RabbitConfig {
    @Value("\${rabbit.username}")
    lateinit var username: String
    @Value("\${rabbit.password}")
    lateinit var password: String

    /**
    Create rabbit connection for messages exchange
     Check if the port is correct
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun createConnection(): Connection? {
        val factory = ConnectionFactory()
        factory.username = username
        factory.password = password
        factory.virtualHost = "/"
        factory.host = "localhost"
        factory.port = 5672
        return factory.newConnection()
    }

    fun closeConnection(connection: Connection, channel: Channel){
        connection.close()
    }

}