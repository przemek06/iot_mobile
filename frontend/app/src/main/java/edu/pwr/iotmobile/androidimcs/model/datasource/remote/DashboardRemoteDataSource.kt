package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DashboardRemoteDataSource {
    @POST("/user/dashboard")
    suspend fun createDashboard(
        @Body dashboardDto: DashboardDto
    ): Response<DashboardDto>

    @DELETE("/user/dashboard/{id}")
    suspend fun deleteDashboard(
        @Path("id") id: Int
    ): Response<ResponseBody>

    @GET("/user/dashboard/{id}")
    suspend fun getDashboardsByProjectId(
        @Path("id") id: Int
    ): Response<List<DashboardDto>>
}