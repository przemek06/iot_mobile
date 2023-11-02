package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.error.exception.TopicAlreadyExistsException
import edu.pwr.iotmobile.error.exception.TopicUsedException
import edu.pwr.iotmobile.rabbit.queue.QueueService
import edu.pwr.iotmobile.repositories.TopicRepository
import org.springframework.stereotype.Service

@Service
class TopicService(
    val topicRepository: TopicRepository,
    val userService: UserService,
    val projectService: ProjectService,
    val queueService: QueueService
) {
    fun createTopic(topic: TopicDTO) : TopicDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        if (!projectService.isEditor(userId, topic.projectId))
            throw NotAllowedException()

        if (topicRepository.existsByUniqueNameAndProjectId(topic.uniqueName, topic.projectId))
            throw TopicAlreadyExistsException()

        queueService.addQueue(topic.name)

        val toSave = topic.toEntityOnCreation()
        return topicRepository.save(toSave).toDTO()
    }

    fun deleteTopic(topicId: Int) : Boolean {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        val topic = topicRepository.findById(topicId)

        if (topic.isEmpty)
            return false

        val projectId = topic.get().project.id ?: return false

        if (!projectService.isEditor(userId, projectId))
            throw NotAllowedException()

        if (isTopicUsed(topicId))
            throw TopicUsedException()

        queueService.forceDeleteQueue(topic.get().name)
        topicRepository.delete(topic.get())

        return true
    }

    fun findAllTopicsInProject(projectId: Int) : List<TopicDTO> {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()

        if (!projectService.isInProject(userId, projectId))
            throw NotAllowedException()

        return topicRepository
            .findAllByProjectId(projectId)
            .map { it.toDTO() }
    }

    fun isTopicUsed(topicId: Int) : Boolean {
        return topicRepository.countTopicUsage(topicId) > 0
    }
}