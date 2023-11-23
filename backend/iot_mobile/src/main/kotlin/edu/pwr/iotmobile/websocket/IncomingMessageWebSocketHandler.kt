package edu.pwr.iotmobile.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.rabbit.RabbitListener
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import reactor.core.Disposable
import reactor.core.publisher.Flux

@Component
@Slf4j
class IncomingMessageWebSocketHandler(
    val rabbitListener: RabbitListener
) : TextWebSocketHandler() {
    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val tagFluxList = registerQueues(message)
        val fluxList = tagFluxList.map { it.second }
        val consumerTags = objectMapper.writeValueAsString(tagFluxList.map { it.first })
        val source = Flux.merge(fluxList)

        val subscription = source
            .subscribe {
                if (session.isOpen) {
                    val responseMessage = objectMapper.writeValueAsString(it)
                    session.sendMessage(TextMessage(responseMessage))
                }
            }

        session.attributes["subscription"] = subscription
        session.attributes["consumerTags"] = consumerTags

    }

    private fun registerQueues(message: TextMessage): List<Pair<String, Flux<MessageDTO>>> {
        if (message.payload.isBlank()) return emptyList()
        return message.payload
            .split(",")
            .map { rabbitListener.registerConsumer(it) }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val subscription = session.attributes["subscription"]
        val consumerTags = objectMapper.readValue(session.attributes["consumerTags"].toString(), List::class.java)
        consumerTags.forEach { rabbitListener.cancelConsumer(it.toString()) }

        if (subscription != null && subscription is Disposable) {
            subscription.dispose()
        }

        session.close()
        super.afterConnectionClosed(session, status)
    }

}