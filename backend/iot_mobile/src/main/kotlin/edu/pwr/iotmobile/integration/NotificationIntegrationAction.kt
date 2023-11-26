package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.GroupMessageDTO
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.service.NotificationService
import edu.pwr.iotmobile.service.ProjectService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NotificationIntegrationAction(
    private val projectService: ProjectService,
    private val notificationService: NotificationService
) : IntegrationAction {

    val logger: Logger = LoggerFactory.getLogger("NotificationIntegrationAction")

    override fun performAction(data: MessageDTO) {
        try {
            val projectId = data.topic.projectId
            val userIds = projectService
                .findAllUsersByProjectIdNoSecurity(projectId)
                .map { it.id }

            val groupMessage = GroupMessageDTO(data, userIds)
            notificationService.processEntityChange(groupMessage)

        } catch (e: java.lang.Exception) {
            logger.error("Error when sending a notification.", e)
        }
    }
}