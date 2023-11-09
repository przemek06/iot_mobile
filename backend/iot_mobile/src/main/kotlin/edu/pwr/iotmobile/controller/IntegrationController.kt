package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.DiscordChannelDTO
import edu.pwr.iotmobile.dto.DiscordUrlDTO
import edu.pwr.iotmobile.service.IntegrationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class IntegrationController(val integrationService: IntegrationService) {

//    @PostMapping("/integration/gmail")
//    fun sendMailMessage(@RequestBody dto: MailMessageDTO): ResponseEntity<Unit> {
//        return ResponseEntity.ok(integrationService.sendMailMessage(dto))
//    }

    @GetMapping("/anon/discord/oauth")
    fun getDiscordOAuthUrl(): ResponseEntity<DiscordUrlDTO> {
        return ResponseEntity.ok(integrationService.getDiscordOAuthUrl())
    }

    @GetMapping("/anon/discord")
    fun discordCallback(@RequestParam("guild_id") guildId: String, @RequestParam state: String): ResponseEntity<Unit> {
        return ResponseEntity.ok(integrationService.discordCallback(state, guildId))
    }

    @GetMapping("/anon/discord/channels/{key}")
    fun listChannels(@PathVariable key: String): ResponseEntity<List<DiscordChannelDTO>> {
        return ResponseEntity.ok(integrationService.listChannels(key))
    }

    @GetMapping("/anon/discord/msg/{channelId}/{message}")
    fun sendDiscordMessage(@PathVariable channelId: String, @PathVariable message: String): ResponseEntity<Unit> {
        return ResponseEntity.ok(integrationService.sendDiscordMessage(channelId, message))
    }
}