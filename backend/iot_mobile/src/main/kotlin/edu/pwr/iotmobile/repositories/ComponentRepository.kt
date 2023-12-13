package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Component
import edu.pwr.iotmobile.enums.EComponentType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ComponentRepository : JpaRepository<Component, Int> {
    
    fun deleteAllByDashboardId(dashboardId: Int)
    fun findAllByDashboardId(dashboardId: Int): List<Component>
    fun findAllByDashboardProjectId(projectId: Int) : List<Component>
    fun findAllByDashboardProjectCreatedById(userId: Int) : List<Component>

}