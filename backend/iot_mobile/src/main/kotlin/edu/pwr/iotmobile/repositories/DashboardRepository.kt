package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Dashboard
import org.springframework.data.jpa.repository.JpaRepository

interface DashboardRepository: JpaRepository<Dashboard, Int> {
}