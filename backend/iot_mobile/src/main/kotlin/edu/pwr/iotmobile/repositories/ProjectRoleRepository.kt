package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.ProjectRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRoleRepository : JpaRepository<ProjectRole, Int> {
    fun findAllByUserId(id: Int) : List<ProjectRole>
    fun findAllByProjectId(id: Int) : List<ProjectRole>
}