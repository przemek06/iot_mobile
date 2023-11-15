package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Component
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComponentRepository : JpaRepository<Component, Int> {
    
    fun deleteAllByDashboardId(dashboardId: Int)
    fun findAllByDashboardId(dashboardId: Int): List<Component>
}