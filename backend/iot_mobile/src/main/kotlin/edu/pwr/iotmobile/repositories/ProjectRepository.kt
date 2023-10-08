package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository: JpaRepository<Project, Int> {
    fun findByProjectRoles_UserId(userId: Int): List<Project>
}