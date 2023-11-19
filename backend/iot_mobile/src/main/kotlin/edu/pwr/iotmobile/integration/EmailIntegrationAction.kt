package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.service.MailService
import edu.pwr.iotmobile.service.ProjectService

class EmailIntegrationAction(
    private val mailService: MailService,
    private val projectService: ProjectService,
    private val projectId: Int,
    private val pattern: String,
    private val subject: String
) : IntegrationAction {

    override fun performAction(data: String) {
        val users = projectService
            .findAllUsersByProjectId(projectId)

        users.forEach {
            val message = pattern.format(data, it.name)
            mailService.sendHtmlMail(subject, it.email, message)
        }

    }
}