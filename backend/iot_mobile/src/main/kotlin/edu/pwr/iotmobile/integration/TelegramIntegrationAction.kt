package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.error.exception.TelegramException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TelegramIntegrationAction(
    private val token: String,
    private val telegramBot: TelegramBot
) : IntegrationAction {

    private val logger: Logger = LoggerFactory.getLogger("TelegramIntegrationAction")

    override fun performAction(data: String) {
        try {
            telegramBot.sendMessageToChannel(token, data)
        } catch (e: TelegramException) {
            logger.error("Cannot send telegram message", e)
        }
    }

}