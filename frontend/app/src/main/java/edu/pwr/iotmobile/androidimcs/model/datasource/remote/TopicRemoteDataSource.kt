package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TopicRemoteDataSource {
    @POST("/user/topic")
    suspend fun createTopic(
        @Body topicDto: TopicDto
    ): Response<TopicDto>

    @DELETE("/user/topic/{id}")
    suspend fun deleteTopic(
        @Path("id") id: Int
    ): Response<ResponseBody>

    @GET("/user/topic/{id}")
    suspend fun getTopicsByProjectId(
        @Path("id") id: Int
    ): Response<List<TopicDto>>
}