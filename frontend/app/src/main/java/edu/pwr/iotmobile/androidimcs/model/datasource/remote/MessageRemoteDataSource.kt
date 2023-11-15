package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.MessageDto
import edu.pwr.iotmobile.androidimcs.data.dto.TopicMessagesDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageRemoteDataSource {

    @POST("/user/messages")
    fun sendMessage(
        @Body message: MessageDto
    ): Response<MessageDto>

    @GET("/user/messages/{dashboardId}/{n}")
    fun getLastMessagesForDashboard(
        @Path("dashboardId") dashboardId: Int,
        @Path("n") numberOfMessages: Int
    ): Response<List<TopicMessagesDto>>

}