package edu.pwr.iotmobile.integration

class DiscordIntegrationAction(
    private val discordBot: DiscordBot,
    private val channelId: String,
    private val pattern: String
) :
    IntegrationAction {

    override fun performAction(data: String) {
        val message = pattern.format(data)
        discordBot.sendMessageToChannel(channelId, message)
    }
}