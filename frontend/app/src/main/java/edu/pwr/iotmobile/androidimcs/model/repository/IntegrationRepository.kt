package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.DiscordChannelDto

interface IntegrationRepository {
    suspend fun getDiscordUrl(): String?
    suspend fun getDiscordChannels(guildId: String): List<DiscordChannelDto>
}