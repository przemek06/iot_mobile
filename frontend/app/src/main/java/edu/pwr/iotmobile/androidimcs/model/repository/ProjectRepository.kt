package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDto
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDtoSend
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult

interface ProjectRepository {

    suspend fun createProject(projectDto: ProjectDto): CreateResult
    suspend fun regenerateConnectionKey(projectId: Int): Result<ProjectDto> // 401 - not allowed
    suspend fun getProjects(): List<ProjectDto>
    suspend fun getUsersByProjectId(id: Int): List<UserInfoDto>
    suspend fun getProjectById(id: Int): ProjectDto?
    suspend fun getUserProjectRole(id: Int): ProjectRoleDto?
    suspend fun deleteProject(id: Int): Result<Unit>
    suspend fun findAllPendingInvitationsForActiveUser(): List<InvitationDto>
    suspend fun acceptInvitation(id: Int): Result<InvitationDto>
    suspend fun rejectInvitation(id: Int): Result<InvitationDto>
    suspend fun createInvitation(invitationDtoSend: InvitationDtoSend): CreateResult
    suspend fun editProjectRole(projectRoleDto: ProjectRoleDto): Result<ProjectRoleDto>
    suspend fun getAllProjectRolesByProjectId(projectId: Int): List<ProjectRoleDto>
    suspend fun revokeAccess(
        projectId: Int,
        userId: Int
    ): Result<Unit>
    suspend fun addProjectAdmin(
        projectId: Int,
        userId: Int
    ): Result<ProjectRoleDto>
}