package edu.pwr.iotmobile.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.dto.TopicMessagesDTO
import edu.pwr.iotmobile.error.exception.*
import edu.pwr.iotmobile.rabbit.queue.QueueService
import edu.pwr.iotmobile.repositories.MessageRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Slf4j
class MessageService (
    val messageRepository: MessageRepository,
    val userService: UserService,
    val dashboardService: DashboardService,
    val projectService: ProjectService,
    val componentService: ComponentService,
    val topicService: TopicService,
    val queueService: QueueService
){
    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

    /**
     * Send message to specified queue
     */
    @Transactional
    fun sendMessage(message: MessageDTO): MessageDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val topic = topicService.findTopic(message.topic.id ?: throw InvalidDataException())

        if (!projectService.isInProject(userId, topic.project.id ?: throw InvalidStateException()))
            throw NotAllowedException()

        val saved = messageRepository.save(message.toEntity())
        val messageStr = objectMapper.writeValueAsString(message)

        try {
            queueService.sendMessage(message.topic.uniqueName, messageStr)
            return saved.toDTO()
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    @Transactional
    fun sendMessageFromDevice(message: MessageDTO): MessageDTO {
        val topic = topicService.findTopic(message.topic.id ?: throw InvalidDataException())

        if (message.connectionKey != topic.project.connectionKey)
            throw NotAllowedException()

        val saved = messageRepository.save(message.toEntity())
        val messageStr = objectMapper.writeValueAsString(message)

        try {
            queueService.sendMessage(message.topic.uniqueName, messageStr)
            return saved.toDTO()
        } catch (_: Exception) {
            throw QueueException()
        }
    }

    fun getLastMessagesForDashboard(dashboardId: Int, n: Int) : List<TopicMessagesDTO> {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val dashboard = dashboardService.findById(dashboardId)

        if (!projectService.isInProject(userId, dashboard.projectId))
            throw NotAllowedException()

        val topicIds = componentService
            .findAllByDashboardId(dashboardId)
            .components
            .mapNotNull { it.topic?.id }
            .distinct()

        val lastMessages = messageRepository
            .findNLastMessagesForTopics(topicIds, n)
            .map { it.toDTO() }
            .filter { it.topic.id != null }
            .groupBy { it.topic.id!! }
            .map { TopicMessagesDTO(it.key, it.value) }

        return lastMessages
    }

}