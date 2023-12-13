package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.error.exception.TelegramException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TelegramIntegrationAction(
    private val token: String,
    private val telegramBot: TelegramBot,
    private val pattern: String,
    private val chatIds: List<String>
) : IntegrationAction {

    private val logger: Logger = LoggerFactory.getLogger("TelegramIntegrationAction")

    override fun performAction(data: MessageDTO) {
        for (chatId in chatIds) {
            try {
                val message = pattern.format(data.message)
                telegramBot.sendMessageToChannel(token, message, chatId)
            } catch (e: TelegramException) {
                logger.error("Cannot send telegram message", e)
            }
        }
    }

}