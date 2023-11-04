package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.ProjectDeletedDTO
import edu.pwr.iotmobile.service.ProjectDeletedNotificationService
import kotlinx.coroutines.flow.Flow
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class ProjectDeletedController(
        val projectDeletedNotificationService: ProjectDeletedNotificationService
) {
    @MessageMapping("deletedProject")
    fun requestResponse(): Flow<ProjectDeletedDTO> = projectDeletedNotificationService.getFlow()
}