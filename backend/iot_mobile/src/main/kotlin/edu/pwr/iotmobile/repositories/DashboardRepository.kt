package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Dashboard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DashboardRepository : JpaRepository<Dashboard, Int> {
    fun findAllByProjectId(projectId: Int) : List<Dashboard>
    fun existsByNameAndProjectId(name: String, projectId: Int) : Boolean
}