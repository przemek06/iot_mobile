package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.DiscordChannelDTO
import edu.pwr.iotmobile.dto.DiscordUrlDTO
import edu.pwr.iotmobile.dto.MailMessageDTO
import edu.pwr.iotmobile.entities.DiscordIntermediate
import edu.pwr.iotmobile.error.exception.DiscordNotFoundException
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.integration.DiscordBot
import edu.pwr.iotmobile.repositories.DiscordIntermediateRepository
import org.springframework.stereotype.Service

const val url1 = "https://discord.com/api/oauth2/authorize?client_id=1172162156050006117&permissions=3072&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fanon%2Fdiscord"
const val url2 = "&response_type=code&scope=identify%20bot"

// Will be called from the outside Trigger Service
@Service
class IntegrationService(
    val mailService: MailService,
    val discordBot: DiscordBot,
    val discordIntermediateRepository: DiscordIntermediateRepository
) {
    fun sendMailMessage(dto: MailMessageDTO) {
        val content = String.format(dto.content, dto.data)
        mailService.sendPlainTextMail(dto.subject, dto.recipient, content)
    }

    fun getDiscordOAuthUrl(): DiscordUrlDTO {
        val toSave = DiscordIntermediate()
        val saved = discordIntermediateRepository.save(toSave)
        val url = url1 + "&state=${saved.id}" + url2
        return DiscordUrlDTO(url, saved.id ?: throw InvalidStateException())
    }

    fun discordCallback(key: String, guildId: String) {
        val entity = discordIntermediateRepository.findById(key).orElseThrow{ DiscordNotFoundException() }
        entity.guildId = guildId
        discordIntermediateRepository.save(entity)
    }

    fun listChannels(key: String) : List<DiscordChannelDTO> {
        val entity = discordIntermediateRepository.findById(key).orElseThrow{ DiscordNotFoundException() }
        return discordBot.getAllChannels(entity.guildId ?: throw DiscordNotFoundException())
    }

    fun sendDiscordMessage(channelId: String, message: String) {
        return discordBot.sendMessageToChannel(channelId, message)
    }
}