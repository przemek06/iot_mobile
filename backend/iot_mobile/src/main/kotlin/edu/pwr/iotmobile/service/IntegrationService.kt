package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.DiscordChannelDTO
import edu.pwr.iotmobile.dto.UriDTO
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.integration.DiscordBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


// Will be called from the outside Trigger Service
@Service
class IntegrationService(
    val mailService: MailService,
    val discordBot: DiscordBot,
//    val discordIntermediateRepository: DiscordIntermediateRepository
) {

    @Value("\${discord.oauth.url}")
    var oauthUrl: String? = null

//    fun sendMailMessage(dto: MailMessageDTO) {
//        val content = String.format(dto.content, dto.data)
//        mailService.sendPlainTextMail(dto.subject, dto.recipient, content)
//    }

    fun getDiscordOAuthUrl(): UriDTO {
        return UriDTO(oauthUrl?: throw InvalidStateException())
    }

//    fun discordCallback(guildId: String) : Stri {
//        val entity = discordIntermediateRepository.findById(key).orElseThrow{ DiscordNotFoundException() }
//        entity.guildId = guildId
//        discordIntermediateRepository.save(entity)
//    }

    fun listChannels(guildId: String) : List<DiscordChannelDTO> {
        return discordBot.getAllChannels(guildId)
    }

    fun sendDiscordMessage(channelId: String, message: String) {
        return discordBot.sendMessageToChannel(channelId, message)
    }
}