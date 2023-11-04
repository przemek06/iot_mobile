package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.dto.TopicMessagesDTO
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.error.exception.QueueException
import edu.pwr.iotmobile.repositories.MessageRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Slf4j
class MessageService (
    val rabbitTemplate: RabbitTemplate,
    val messageRepository: MessageRepository,
    val userService: UserService,
    val dashboardService: DashboardService,
    val projectService: ProjectService,
    val componentService: ComponentService
){

    /**
     * Send message to specified queue
     */
    @Transactional
    fun sendMessage(message: MessageDTO): MessageDTO {
        try{
            val saved = messageRepository.save(message.toEntity())
            rabbitTemplate.convertAndSend(message.topic.name, message.topic.name, message.message)
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