package edu.pwr.iotmobile.androidimcs.model.repository.impl

import android.util.Log
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDto
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDtoSend
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ProjectRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val remoteDataSource: ProjectRemoteDataSource
) : ProjectRepository {
    override suspend fun createProject(projectDto: ProjectDto): CreateResult {
        val result = remoteDataSource.createProject(projectDto)
        val code = result.code()
        Log.d("ProjectRepo", "createDashboard result code: $code")
        return when (code) {
            200 -> CreateResult.Success
            401 -> CreateResult.NotAuthorized
            409 -> CreateResult.AlreadyExists
            else -> CreateResult.Failure
        }
    }

    override suspend fun regenerateConnectionKey(projectId: Int): Result<ProjectDto> {
        val result = remoteDataSource.regenerateConnectionKey(projectId)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            Result.success(body)
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

    override suspend fun deleteProject(id: Int): Result<Unit> {
        val result = remoteDataSource.deleteProjectById(id)
        return if (result.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Delete project failed"))
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

    override suspend fun editProjectRole(projectRoleDto: ProjectRoleDto): Result<ProjectRoleDto> {
        val result = remoteDataSource.editProjectRole(projectRoleDto)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Editing project role failed"))
    }

    override suspend fun getAllProjectRolesByProjectId(projectId: Int): List<ProjectRoleDto> {
        val result = remoteDataSource.findAllProjectRolesByProjectId(projectId)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }

    override suspend fun revokeAccess(projectId: Int, userId: Int): Result<Unit> {
        val result = remoteDataSource.revokeAccess(
            projectId = projectId,
            userId = userId
        )
        val body = result.body()
        return if (result.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Revoke access failed"))
    }

    override suspend fun addProjectAdmin(projectId: Int, userId: Int): Result<ProjectRoleDto> {
        val result = remoteDataSource.addProjectAdmin(
            projectId = projectId,
            userId = userId
        )
        val body = result.body()
        return if (result.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Adding admin failed"))
    }
}