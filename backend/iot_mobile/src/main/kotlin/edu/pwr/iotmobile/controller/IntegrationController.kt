package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.DiscordChannelDTO
import edu.pwr.iotmobile.dto.UriDTO
import edu.pwr.iotmobile.service.IntegrationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI


@RestController
class IntegrationController(val integrationService: IntegrationService) {

    @Value("\${discord.android.redirect}")
    var redirectUri: String? = null

//    @PostMapping("/integration/gmail")
//    fun sendMailMessage(@RequestBody dto: MailMessageDTO): ResponseEntity<Unit> {
//        return ResponseEntity.ok(integrationService.sendMailMessage(dto))
//    }

    @GetMapping("/user/discord/oauth")
    fun getDiscordOAuthUrl(): ResponseEntity<UriDTO> {
        return ResponseEntity.ok(integrationService.getDiscordOAuthUrl())
    }

    @GetMapping("/anon/discord")
    fun discordCallback(@RequestParam("guild_id") guildId: String): String {
        return "redirect:$redirectUri?type=discord&id=$guildId"
    }

    @GetMapping("/user/discord/channels/{guildId}")
    fun listChannels(@PathVariable guildId: String): ResponseEntity<List<DiscordChannelDTO>> {
        return ResponseEntity.ok(integrationService.listChannels(guildId))
    }

//    @GetMapping("/user/discord/msg/{channelId}/{message}")
//    fun sendDiscordMessage(@PathVariable channelId: String, @PathVariable message: String): ResponseEntity<Unit> {
//        return ResponseEntity.ok(integrationService.sendDiscordMessage(channelId, message))
//    }
}