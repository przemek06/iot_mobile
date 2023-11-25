package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.DiscordChannelDto

interface IntegrationRepository {
    suspend fun getDiscordKey(): String?
    suspend fun getDiscordUrl(key: String): String?
    suspend fun getGuildId(key: String): String?
    suspend fun getDiscordChannels(guildId: String): List<DiscordChannelDto>
}