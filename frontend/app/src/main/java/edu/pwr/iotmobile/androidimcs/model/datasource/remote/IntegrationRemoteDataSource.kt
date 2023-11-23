package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.DiscordChannelDto
import edu.pwr.iotmobile.androidimcs.data.dto.UriDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IntegrationRemoteDataSource {
    @GET("/user/discord/oauth")
    suspend fun getDiscordUrl(): Response<UriDto>


    @GET("/user/discord/channels/{id}")
    suspend fun getDiscordChannels(
        @Path("id") guildId: String
    ): Response<List<DiscordChannelDto>>
}