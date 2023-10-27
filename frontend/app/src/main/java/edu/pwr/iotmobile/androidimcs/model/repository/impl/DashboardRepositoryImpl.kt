package edu.pwr.iotmobile.androidimcs.model.repository.impl

import android.util.Log
import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateDashboardResult
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.DashboardRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository

private const val TAG = "DashboardRepo"

class DashboardRepositoryImpl(
    private val remoteDataSource: DashboardRemoteDataSource
) : DashboardRepository {
    override suspend fun createDashboard(dashboardDto: DashboardDto): CreateDashboardResult {
        val result = remoteDataSource.createDashboard(dashboardDto)
        val code = result.code()
        Log.d(TAG, "createDashboard result code: $code")
        return when (code) {
            200 -> CreateDashboardResult.Success
            401 -> CreateDashboardResult.NotAuthorized
            409 -> CreateDashboardResult.AlreadyExists
            else -> CreateDashboardResult.Failure
        }
    }

    override suspend fun deleteDashboard(id: Int): Result<Unit> {
        val result = remoteDataSource.deleteDashboard(id)
        return if (result.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Delete dashboard failed."))
    }

    override suspend fun getDashboardsByProjectId(projectId: Int): List<DashboardDto> {
        val result = remoteDataSource.getDashboardsByProjectId(projectId)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }
}