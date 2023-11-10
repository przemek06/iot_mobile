package edu.pwr.iotmobile.androidimcs.model.repository.impl

import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDto
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDtoSend
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ProjectRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val remoteDataSource: ProjectRemoteDataSource
) : ProjectRepository {
    override suspend fun createProject(projectDto: ProjectDto): Result<Unit> {
        val result = remoteDataSource.createProject(projectDto)
        return if (result.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Create project failed"))
    }

    override suspend fun regenerateConnectionKey(projectId: Int): Result<Unit> {
        val result = remoteDataSource.regenerateConnectionKey(projectId)
        return if (result.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Regenerate connection key failed"))
    }

    override suspend fun getProjects(): List<ProjectDto> {
        val result = remoteDataSource.getProjects()
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }

    override suspend fun getUsersByProjectId(id: Int): List<UserInfoDto> {
        val result = remoteDataSource.getUsersByProjectId(id)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }

    override suspend fun getProjectById(id: Int): ProjectDto? {
        val result = remoteDataSource.findProjectById(id)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            null
    }

    override suspend fun getUserProjectRole(id: Int): ProjectRoleDto? {
        val result = remoteDataSource.findActiveUserProjectRole(id)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            null
    }

    override suspend fun findAllPendingInvitationsForActiveUser(): List<InvitationDto> {
        val result = remoteDataSource.findAllPendingInvitationsForActiveUser()
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }

    override suspend fun acceptInvitation(id: Int): Result<InvitationDto> {
        val result = remoteDataSource.acceptInvitation(id)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Accepting invitation failed"))
    }

    override suspend fun rejectInvitation(id: Int): Result<InvitationDto> {
        val result = remoteDataSource.rejectInvitation(id)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Rejecting invitation failed"))
    }

    override suspend fun createInvitation(invitationDtoSend: InvitationDtoSend): Result<InvitationDto> {
        val result = remoteDataSource.createInvitation(invitationDtoSend)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Create invitation failed"))
    }
}