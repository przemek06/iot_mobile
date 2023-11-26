package edu.pwr.iotmobile.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
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
import reactor.core.publisher.Flux

@Component
@Slf4j
class IncomingMessageWebSocketHandler(
    val rabbitListener: RabbitListener,
    val projectService: ProjectService,
    val topicService: TopicService,
    val userService: UserService
) : TextWebSocketHandler() {
    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val userId = userService.findUserByEmail(session.principal?.name ?: throw NoAuthenticationException())?.id
            ?: throw NoAuthenticationException()

        val topics = getTopicList(message)

        if (!hasAccess(userId, topics)) throw NotAllowedException()

        val connectionKey = retrieveConnectionKey(topics)

        val tagFluxList = registerQueues(message, connectionKey)
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

    fun getTopicList(message: TextMessage) : List<Topic> {
        val topicNames = splitTopics(message)
        return topicService.findAllByUniqueNames(topicNames)
    }

    fun retrieveConnectionKey(topics: List<Topic>) : String {
        val connectionKeys = topics.map { it.project.connectionKey }.distinct()

        if (connectionKeys.size != 1) {
            throw InvalidStateException()
        }

        return connectionKeys[0]
    }

    fun hasAccess(userId: Int, topics: List<Topic>) : Boolean {
        val topicIds = topics.map { it.project.id }.distinct()

        if (topicIds.size != 1) {
            return false
        }

        val projectId = topicIds[0] ?: return false

        return projectService.isInProject(userId, projectId)
    }

    fun splitTopics(message: TextMessage): List<String> {
        if (message.payload.isBlank()) return emptyList()
        return message.payload
            .split(",")
    }

    private fun registerQueues(message: TextMessage, connectionKey: String): List<Pair<String, Flux<MessageDTO>>> {
        return splitTopics(message)
            .map { rabbitListener.registerConsumer(it, connectionKey) }
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