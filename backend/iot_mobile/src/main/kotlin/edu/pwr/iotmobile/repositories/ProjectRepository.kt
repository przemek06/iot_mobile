package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Int> {
    fun findAllByCreatedById(userId: Int) : List<Project>
}