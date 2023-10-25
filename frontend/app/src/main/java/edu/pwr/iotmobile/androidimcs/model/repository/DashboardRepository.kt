package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto

interface DashboardRepository {
    suspend fun createDashboard(dashboardDto: DashboardDto)
    suspend fun deleteDashboard(id: Int): Result<Unit>
    suspend fun getDashboardsByProjectId(projectId: Int): List<DashboardDto>
}