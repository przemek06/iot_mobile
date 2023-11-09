package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.DiscordChannelDTO
import net.dv8tion.jda.api.JDABuilder
import org.springframework.stereotype.Component


@Component
class DiscordBot {
    val jda = JDABuilder.createDefault("MTE3MjE2MjE1NjA1MDAwNjExNw.GazWet.jg9nIqNITZLx8aVFYM_3aLKSNQnXSqtWM_OkbY").build()

    fun sendMessageToChannel(channelId: String, message: String) {
        val targetChannel = jda.getTextChannelById(channelId)
        targetChannel?.sendMessage(message)?.queue()
    }

    fun getAllChannels(guildId: String) : List<DiscordChannelDTO> {
        val channels = jda.getGuildById(guildId)?.channels

        return channels?.map { DiscordChannelDTO(it.name, it.id) } ?: emptyList()
    }

}