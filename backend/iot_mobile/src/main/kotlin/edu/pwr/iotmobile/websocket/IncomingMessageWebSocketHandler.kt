package edu.pwr.iotmobile.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import edu.pwr.iotmobile.rabbit.RabbitListener
import edu.pwr.iotmobile.service.IncomingMessageService
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import reactor.core.Disposable

@Component
@Slf4j
class IncomingMessageWebSocketHandler(
    val incomingMessageService: IncomingMessageService,
    val rabbitListener: RabbitListener
): TextWebSocketHandler() {
    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
    private val logger: Logger = LoggerFactory.getLogger("Websocket message handler")
    private var queues : MutableSet<String> = LinkedHashSet()



    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val queueList: List<String> = registerQueues(message)

        val subscription = incomingMessageService
            .getEntityChangeFlow(queueList)
            .subscribe {
                if (session.isOpen) {
                    val responseMessage = objectMapper.writeValueAsString(it)
                    session.sendMessage(TextMessage(responseMessage))
                }
            }
        logger.info("Opened new connection for topics: $queueList")
        session.attributes["subscription"] = subscription
    }

    //TODO: maybe better format./ maybe its enough
    private fun registerQueues(message: TextMessage): List<String> {
        val queueList: List<String> = message.payload.split(",")
        val queueNames = queueList.stream().filter { !queues.contains(it) }.toList()
        rabbitListener.registerConsumer(queueNames)
        queues.addAll(queueList)
        return queueList
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val subscription = session.attributes["subscription"]
        if (subscription != null && subscription is Disposable) {
            subscription.dispose()
        }
        session.close()
        super.afterConnectionClosed(session, status)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
    }
}