package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.error.exception.SlackException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlackIntegrationAction(
    private val webhook: String,
    private val slackBot: SlackBot
) : IntegrationAction {

    private val logger: Logger = LoggerFactory.getLogger("SlackIntegrationAction")

    override fun performAction(data: MessageDTO) {
        try {
            slackBot.sendMessageToChannel(webhook, data.message)
        } catch (e: SlackException) {
            logger.error("Cannot send slack message", e)
        }
    }

}