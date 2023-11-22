package edu.pwr.iotmobile.androidimcs.model.repository.impl

import edu.pwr.iotmobile.androidimcs.data.dto.DiscordChannelDto
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.IntegrationRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.IntegrationRepository

class IntegrationRepositoryImpl(
    private val remoteDataSource: IntegrationRemoteDataSource
) : IntegrationRepository {
    override suspend fun getDiscordUrl(): String? {
        val result = remoteDataSource.getDiscordUrl()
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else null
    }

    override suspend fun getDiscordChannels(guildId: String): List<DiscordChannelDto> {
        val result = remoteDataSource.getDiscordChannels(guildId)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else emptyList()
    }
}