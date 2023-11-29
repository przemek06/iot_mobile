package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.error.exception.TelegramException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TelegramIntegrationAction(
    private val token: String,
    private val telegramBot: TelegramBot
) : IntegrationAction {

    private val logger: Logger = LoggerFactory.getLogger("TelegramIntegrationAction")

    override fun performAction(data: MessageDTO) {
        try {
            telegramBot.sendMessageToChannel(token, data.message)
        } catch (e: TelegramException) {
            logger.error("Cannot send telegram message", e)
        }
    }

}