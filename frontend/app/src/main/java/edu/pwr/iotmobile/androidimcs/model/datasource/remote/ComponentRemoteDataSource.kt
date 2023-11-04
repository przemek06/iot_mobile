package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ComponentRemoteDataSource {

    @PUT("/user/component")
    suspend fun updateComponentList(
        @Body componentListDto: ComponentListDto
    ): Response<ComponentListDto>

    @GET("/user/component/{id}")
    suspend fun getComponentList(
        @Path("id") dashboardId: Int
    ): Response<ComponentListDto>
}