package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.DiscordChannelDto
import edu.pwr.iotmobile.androidimcs.data.dto.KeyDto
import edu.pwr.iotmobile.androidimcs.data.dto.UriDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IntegrationRemoteDataSource {
    @GET("/user/discord/key")
    suspend fun getDiscordKey(): Response<KeyDto>

    @GET("/user/discord/oauth/{key}")
    suspend fun getDiscordUrl(
        @Path("key") key: String
    ): Response<UriDto>

    @GET("/user/discord/guild/{key}")
    suspend fun getGuildId(
        @Path("key") key: String
    ): Response<KeyDto>

    @GET("/user/discord/channels/{id}")
    suspend fun getDiscordChannels(
        @Path("id") guildId: String
    ): Response<List<DiscordChannelDto>>
}