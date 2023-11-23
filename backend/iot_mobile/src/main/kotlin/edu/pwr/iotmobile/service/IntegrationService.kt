package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.DiscordChannelDTO
import edu.pwr.iotmobile.dto.KeyDTO
import edu.pwr.iotmobile.dto.UriDTO
import edu.pwr.iotmobile.entities.DiscordIntermediate
import edu.pwr.iotmobile.error.exception.DiscordNotFoundException
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.integration.DiscordBot
import edu.pwr.iotmobile.repositories.DiscordIntermediateRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


// Will be called from the outside Trigger Service
@Service
class IntegrationService(
    val mailService: MailService,
    val discordBot: DiscordBot,
    val discordIntermediateRepository: DiscordIntermediateRepository
) {

    @Value("\${discord.oauth.url}")
    var oauthUrl: String? = null

//    fun sendMailMessage(dto: MailMessageDTO) {
//        val content = String.format(dto.content, dto.data)
//        mailService.sendPlainTextMail(dto.subject, dto.recipient, content)
//    }

    fun getDiscordOAuthUrl(key: String): UriDTO {
        val uri = "$oauthUrl&state=$key"
        return UriDTO(uri)
    }

    fun getDiscordKey() : KeyDTO {
        val intermediate = discordIntermediateRepository.save(DiscordIntermediate())
        return KeyDTO(intermediate.id ?: throw InvalidStateException())
    }

    fun discordCallback(guildId: String, key: String) {
        val entity = discordIntermediateRepository.findById(key).orElseThrow{ DiscordNotFoundException() }
        entity.guildId = guildId
        discordIntermediateRepository.save(entity)
    }

    fun getGuildId(key: String) : KeyDTO {
        return KeyDTO(discordIntermediateRepository.findById(key).orElseThrow {DiscordNotFoundException()}.guildId ?: throw DiscordNotFoundException())
    }

    fun listChannels(guildId: String) : List<DiscordChannelDTO> {
        return discordBot.getAllChannels(guildId)
    }

//    fun sendDiscordMessage(channelId: String, message: String) {
//        return discordBot.sendMessageToChannel(channelId, message)
//    }
}