package edu.pwr.iotmobile.androidimcs.model.repository.impl

import edu.pwr.iotmobile.androidimcs.data.dto.DiscordChannelDto
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.IntegrationRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.IntegrationRepository

class IntegrationRepositoryImpl(
    private val remoteDataSource: IntegrationRemoteDataSource
) : IntegrationRepository {
    override suspend fun getDiscordKey(): String? {
        val result = remoteDataSource.getDiscordKey()
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body.key
        else null
    }

    override suspend fun getDiscordUrl(key: String): String? {
        val result = remoteDataSource.getDiscordUrl(key)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body.uri
        else null
    }

    override suspend fun getGuildId(key: String): String? {
        val result = remoteDataSource.getGuildId(key)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body.key
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