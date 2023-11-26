package edu.pwr.iotmobile.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import edu.pwr.iotmobile.dto.TopicConnectionDTO
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.rabbit.RabbitListener
import edu.pwr.iotmobile.service.ProjectService
import edu.pwr.iotmobile.service.TopicService
import edu.pwr.iotmobile.service.UserService
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import reactor.core.Disposable

@Component
@Slf4j
class DeviceIncomingMessageWebSocketHandler(
    val rabbitListener: RabbitListener,
    val projectService: ProjectService,
    val topicService: TopicService,
    val userService: UserService
) : TextWebSocketHandler() {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val topicConnection = objectMapper.readValue<TopicConnectionDTO>(message.payload)
        if (!hasAccess(topicConnection.uniqueName, topicConnection.connectionKey)) throw NotAllowedException()

        val pair = rabbitListener.registerConsumer(topicConnection.uniqueName)
        val consumerTag = pair.first
        val source = pair.second

        val subscription = source
            .subscribe {
                if (session.isOpen) {
                    val responseMessage = objectMapper.writeValueAsString(it)
                    session.sendMessage(TextMessage(responseMessage))
                }
            }

        session.attributes["subscription"] = subscription
        session.attributes["consumerTag"] = consumerTag
    }

    fun hasAccess(topic: String, connectionKey: String) : Boolean {
        return topicService.findByUniqueName(topic).project.connectionKey == connectionKey
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val subscription = session.attributes["subscription"]
        val consumerTag = session.attributes["consumerTag"].toString()

        rabbitListener.cancelConsumer(consumerTag)
        if (subscription != null && subscription is Disposable) {
            subscription.dispose()
        }

        session.close()
        super.afterConnectionClosed(session, status)
    }

}