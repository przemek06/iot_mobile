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
class RabbitAdminConfig() {

    @Value("\${rabbit.username}")
    lateinit var username: String
    @Value("\${rabbit.password}")
    lateinit var password: String

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin{
        return RabbitAdmin(connectionFactory)
    }
    @Bean
    fun  rabbitListenerEndpointRegistry():RabbitListenerEndpointRegistry{
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
        factory.virtualHost = "/"
        factory.host = "localhost"
        factory.port = 5672
        return factory.newConnection()
    }


    fun closeConnection(connection: Connection){
        connection.close()
    }


}