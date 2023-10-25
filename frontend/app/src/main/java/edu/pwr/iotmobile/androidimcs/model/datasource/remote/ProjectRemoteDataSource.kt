package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectRemoteDataSource {

    @POST("/user/project")
    suspend fun createProject(
        @Body projectDto: ProjectDto
    ): Response<ProjectDto>

    @PUT("/user/project/key/{id}")
    suspend fun regenerateConnectionKey(
        @Path("id") projectId: Int
    ): Response<ProjectDto>

    @GET("/user/project/active")
    suspend fun getProjects(): Response<List<ProjectDto>>

    @GET("/user/project/users/{id}")
    suspend fun getUsersByProjectId(
        @Path("id") id: Int
    ): Response<List<UserInfoDto>>
}