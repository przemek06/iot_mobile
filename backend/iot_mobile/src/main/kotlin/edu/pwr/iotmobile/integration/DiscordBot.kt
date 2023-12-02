package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.DiscordChannelDTO
import jakarta.annotation.PostConstruct
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.channel.ChannelType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class DiscordBot {

    @Value("\${discord.token}")
    var token: String? = null
    var jda : JDA? = null

    @PostConstruct
    fun init() {
        jda = JDABuilder.createDefault(token).build()
    }

    fun sendMessageToChannel(channelId: String, message: String) {
        val targetChannel = jda?.getTextChannelById(channelId)
        targetChannel?.sendMessage(message)?.queue()
    }

    fun getAllChannels(guildId: String) : List<DiscordChannelDTO> {
        val channels = jda?.getGuildById(guildId)?.channels

        return channels
            ?.filter { it.type == ChannelType.TEXT }
            ?.map { DiscordChannelDTO(it.name, it.id) } ?: emptyList()
    }

}