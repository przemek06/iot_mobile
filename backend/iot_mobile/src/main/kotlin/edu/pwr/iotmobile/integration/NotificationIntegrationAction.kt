package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.GroupNotificationDTO
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.dto.NotificationDTO
import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.service.NotificationService
import edu.pwr.iotmobile.service.ProjectService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NotificationIntegrationAction(
    private val projectService: ProjectService,
    private val notificationService: NotificationService,
    private val triggerComponent: TriggerComponent
) : IntegrationAction {

    val logger: Logger = LoggerFactory.getLogger("NotificationIntegrationAction")

    override fun performAction(data: MessageDTO) {
        try {
            val projectId = data.topic.projectId
            val userIds = projectService
                .findAllUsersByProjectIdNoSecurity(projectId)
                .map { it.id }

            val description = triggerComponent.pattern.format(data.message)
            val title = triggerComponent.actionDestination.token
            val notification = NotificationDTO(title, description, data)
            val groupMessage = GroupNotificationDTO(notification, userIds)

            notificationService.processEntityChange(groupMessage)

        } catch (e: java.lang.Exception) {
            logger.error("Error when sending a notification.", e)
        }
    }
}