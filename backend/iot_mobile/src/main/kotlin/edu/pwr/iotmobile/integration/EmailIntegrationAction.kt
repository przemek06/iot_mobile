package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.service.MailService
import edu.pwr.iotmobile.service.ProjectService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EmailIntegrationAction(
    private val mailService: MailService,
    private val projectService: ProjectService,
    private val projectId: Int,
    private val pattern: String,
    private val subject: String
) : IntegrationAction {

    val logger: Logger = LoggerFactory.getLogger("EmailIntegrationAction")

    override fun performAction(data: String) {
        try {
            val users = projectService
                .findAllUsersByProjectIdNoSecurity(projectId)

            users.forEach {
                val message = pattern.format(data, it.name)
                mailService.sendHtmlMail(subject, it.email, message)
            }
        } catch (e: java.lang.Exception) {
            logger.error("Error during email action execution ignored.", e)
        }
    }
}