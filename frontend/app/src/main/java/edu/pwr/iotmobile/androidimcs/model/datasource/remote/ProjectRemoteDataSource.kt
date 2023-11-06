package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
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

    @GET("/user/project/{id}")
    suspend fun findProjectById(
        @Path("id") id: Int
    ): Response<ProjectDto>

    @GET("/user/project/users/roles/active/{id}")
    suspend fun findActiveUserProjectRole(
        @Path("id") id: Int
    ): Response<ProjectRoleDto>

    @GET("/user/project/invitation/pending/active")
    suspend fun findAllPendingInvitationsForActiveUser(): Response<List<InvitationDto>>

    @GET("/user/project/invitation/accept/{id}")
    suspend fun acceptInvitation(
        @Path("id") id: Int
    ): Response<InvitationDto>

    @GET("/user/project/invitation/reject/{id}")
    suspend fun rejectInvitation(
        @Path("id") id: Int
    ): Response<InvitationDto>
}