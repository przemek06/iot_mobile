package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.entity.DashboardEntity
import edu.pwr.iotmobile.androidimcs.data.result.CreateDashboardResult

interface DashboardRepository {
    suspend fun createDashboard(dashboardDto: DashboardDto): CreateDashboardResult // 401 if not editor, 409 - if already exists
    suspend fun deleteDashboard(id: Int): Result<Unit>
    suspend fun getDashboardsByProjectId(projectId: Int): List<DashboardDto>
    suspend fun getLastAccessedDashboards(): List<DashboardEntity>
    suspend fun saveLastAccessedDashboard(dashboardEntity: DashboardEntity)
}