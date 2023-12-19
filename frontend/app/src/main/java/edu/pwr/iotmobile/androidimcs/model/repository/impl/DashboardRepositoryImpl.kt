package edu.pwr.iotmobile.androidimcs.model.repository.impl

import android.util.Log
import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.entity.DashboardEntity
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult
import edu.pwr.iotmobile.androidimcs.model.datasource.local.dao.DashboardDao
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.DashboardRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository

private const val TAG = "DashboardRepo"

class DashboardRepositoryImpl(
    private val remoteDataSource: DashboardRemoteDataSource,
    private val localDataSource: DashboardDao,
) : DashboardRepository {
    override suspend fun createDashboard(dashboardDto: DashboardDto): CreateResult {
        val result = remoteDataSource.createDashboard(dashboardDto)
        val code = result.code()
        Log.d(TAG, "createDashboard result code: $code")
        return when (code) {
            200 -> CreateResult.Success
            401 -> CreateResult.NotAuthorized
            409 -> CreateResult.AlreadyExists
            else -> CreateResult.Failure
        }
    }

    override suspend fun deleteDashboard(id: Int): Result<Unit> {
        val result = remoteDataSource.deleteDashboard(id)
        return if (result.isSuccessful) {
            localDataSource.deleteDashboardById(id)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Delete dashboard failed."))
        }
    }

    override suspend fun getDashboardsByProjectId(projectId: Int): List<DashboardDto> {
        val result = remoteDataSource.getDashboardsByProjectId(projectId)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }

    override suspend fun getLastAccessedDashboardsByUserId(userId: Int): List<DashboardEntity> = localDataSource.getAllByUserId(userId)
    override suspend fun saveLastAccessedDashboard(dashboardEntity: DashboardEntity) {
        val currentDashboards = localDataSource
            .getByDashboardId(dashboardEntity.dashboardId)
            .toTypedArray()
        if (currentDashboards.isNotEmpty()) {
            localDataSource.deleteDashboards(*currentDashboards)
        }
        localDataSource.insertDashboard(dashboardEntity)
    }
}