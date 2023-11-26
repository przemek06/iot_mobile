package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.error.exception.SlackException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlackIntegrationAction(
    private val webhook: String,
    private val slackBot: SlackBot
) : IntegrationAction {

    private val logger: Logger = LoggerFactory.getLogger("EmailIntegrationAction")

    override fun performAction(data: String) {
        try {
            slackBot.sendMessageToChannel(webhook, data)
        } catch (e: SlackException) {
            logger.error("Cannot send slack message", e)
        }
    }

}