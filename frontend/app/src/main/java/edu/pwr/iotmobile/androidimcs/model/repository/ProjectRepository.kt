package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDto
import edu.pwr.iotmobile.androidimcs.data.dto.InvitationDtoSend
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.ProjectRoleDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto

interface ProjectRepository {

    suspend fun createProject(projectDto: ProjectDto): Result<Unit>
    suspend fun regenerateConnectionKey(projectId: Int): Result<Unit> // 401 - not allowed
    suspend fun getProjects(): List<ProjectDto>
    suspend fun getUsersByProjectId(id: Int): List<UserInfoDto>
    suspend fun getProjectById(id: Int): ProjectDto?
    suspend fun getUserProjectRole(id: Int): ProjectRoleDto?
    suspend fun findAllPendingInvitationsForActiveUser(): List<InvitationDto>
    suspend fun acceptInvitation(id: Int): Result<InvitationDto>
    suspend fun rejectInvitation(id: Int): Result<InvitationDto>
    suspend fun createInvitation(invitationDtoSend: InvitationDtoSend): Result<InvitationDto>
}