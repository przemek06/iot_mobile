package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.MessageDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DiscordIntegrationAction(
    private val discordBot: DiscordBot,
    private val channelId: String,
    private val pattern: String
) :
    IntegrationAction {

    val logger: Logger = LoggerFactory.getLogger("DiscordIntegrationAction")

    override fun performAction(data: MessageDTO) {
        try {
            val message = pattern.format(data.message)
            discordBot.sendMessageToChannel(channelId, message)
        } catch (e: Exception) {
            logger.error("Error during Discord action execution ignored.", e)
        }

    }
}