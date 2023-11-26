package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.error.exception.*
import edu.pwr.iotmobile.rabbit.queue.QueueService
import edu.pwr.iotmobile.repositories.TopicRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TopicService(
    val topicRepository: TopicRepository,
    val userService: UserService,
    val projectService: ProjectService,
    val queueService: QueueService
) {

    fun findTopic(topicId: Int) : Topic {
        return topicRepository
            .findById(topicId)
            .orElseThrow{ TopicNotFoundException() }

    }

    fun createTopic(topic: TopicDTO) : TopicDTO {
        val userId = userService.getActiveUserId() ?: throw NoAuthenticationException()
        if (!projectService.isEditor(userId, topic.projectId))
            throw NotAllowedException()

        if (topicRepository.existsByUniqueNameAndProjectId(topic.uniqueName, topic.projectId))
            throw TopicAlreadyExistsException()

        val toSave = topic.toEntityOnCreation()
        queueService.addExchange(toSave.uniqueName)
        return topicRepository.save(toSave).toDTO()
    }

    @Transactional
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

        topicRepository.delete(topic.get())
        queueService.forceDeleteExchange(topic.get().uniqueName)

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