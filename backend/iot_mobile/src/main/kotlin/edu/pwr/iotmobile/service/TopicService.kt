package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.error.exception.DashboardAlreadyExistsException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.error.exception.TopicAlreadyExistsException
import edu.pwr.iotmobile.repositories.TopicRepository
import org.springframework.stereotype.Service

@Service
class TopicService(
    val topicRepository: TopicRepository,
    val userService: UserService,
    val projectService: ProjectService
) {
    fun createTopic(dashboard: TopicDTO) : TopicDTO {
        val userId = userService.getActiveUserId()
        if (!projectService.isEditor(userId, dashboard.projectId))
            throw NotAllowedException()

        if (topicRepository.existsByNameAndProjectId(dashboard.name, dashboard.projectId))
            throw TopicAlreadyExistsException()

        val toSave = dashboard.toEntity()
        return topicRepository.save(toSave).toDTO()
    }

    fun deleteTopic(dashboardId: Int) : Boolean {
        val userId = userService.getActiveUserId()
        val topic = topicRepository.findById(dashboardId)

        if (topic.isEmpty)
            return false

        val projectId = topic.get().project.id ?: return false

        if (!projectService.isEditor(userId, projectId))
            throw NotAllowedException()

        topicRepository.delete(topic.get())

        return true
    }

    fun findAllTopicsInProject(projectId: Int) : List<TopicDTO> {
        val userId = userService.getActiveUserId()

        if (!projectService.isInProject(userId, projectId))
            throw NotAllowedException()

        return topicRepository
            .findAllByProjectId(projectId)
            .map { it.toDTO() }
    }
}