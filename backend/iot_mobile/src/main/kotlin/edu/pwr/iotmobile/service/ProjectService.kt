package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.Project
import edu.pwr.iotmobile.repositories.ProjectRepository
import edu.pwr.iotmobile.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class ProjectService(
    val projectRepository: ProjectRepository,
    val userRepository: UserRepository
) {
    fun getByUserId(userId: Int): List<Project> {
        return projectRepository.findByProjectRoles_UserId(userId)
    }

}