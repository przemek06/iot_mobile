package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ProjectDTO
import edu.pwr.iotmobile.dto.UserInfoDTO
import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.entities.ProjectRole
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.repositories.ProjectRepository
import edu.pwr.iotmobile.repositories.ProjectRoleRepository
import edu.pwr.iotmobile.security.EProjectRole
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class ProjectService(
    val projectRepository: ProjectRepository,
    val projectRoleRepository: ProjectRoleRepository,
    val userService: UserService
) {
    private fun findAllProjectsByUserId(userId: Int): List<ProjectDTO> {
        return projectRoleRepository
            .findAllByUserId(userId)
            .map { it.project.toDTO() }
    }

    fun findAllProjectsForActiveUser(): List<ProjectDTO> {
        val userId = userService.getActiveUserId()
        return findAllProjectsByUserId(userId)
    }

    private fun generateConnectionKey() : String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..32)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun saveCreatorProjectRole(project: Project) : ProjectRole {
        val projectRole = ProjectRole(project, project.createdBy, EProjectRole.ADMIN)
        return projectRoleRepository.save(projectRole)
    }

    @Transactional
    fun createProject(projectDTO: ProjectDTO) : ProjectDTO {
        val userId = userService.getActiveUserId()

        if (projectDTO.createdBy.id != userId) {
            throw NotAllowedException()
       }

        val project = projectDTO.toEntity()
        project.connectionKey = generateConnectionKey()
        saveCreatorProjectRole(project)

        return projectRepository.save(project).toDTO()
    }

    fun findAllUsersByProjectId(projectId: Int) : List<UserInfoDTO> {
        val projectUserList = projectRoleRepository.findAllByProjectId(projectId)
        val userIdList = projectUserList.map { it.user.id }
        val activeUserId = userService.getActiveUserId()

        if (!userIdList.contains(activeUserId)) {
            throw NotAllowedException()
        }

        return projectUserList.map { it.user.toUserInfoDTO() }
    }
}