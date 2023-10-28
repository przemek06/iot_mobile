package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.ProjectDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto

interface ProjectRepository {

    suspend fun createProject(projectDto: ProjectDto): Result<Unit>
    suspend fun regenerateConnectionKey(projectId: Int): Result<Unit> // 401 - not allowed
    suspend fun getProjects(): List<ProjectDto>
    suspend fun getUsersByProjectId(id: Int): List<UserInfoDto>
}