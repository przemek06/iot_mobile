package edu.pwr.iotmobile.androidimcs.model.repository.impl

import android.util.Log
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentDto
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ComponentRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.ComponentRepository

class ComponentRepositoryImpl(
    private val remoteDataSource: ComponentRemoteDataSource
) : ComponentRepository {
    override suspend fun updateComponentList(componentListDto: ComponentListDto): Result<Unit> {
        val result = remoteDataSource.updateComponentList(componentListDto)
        Log.d("body", "body")
        Log.d("body", result.body()?.toString() ?: "")
        return if (result.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Update component list failed."))
    }

    override suspend fun getComponentList(dashboardInt: Int): List<ComponentDto> {
        val result = remoteDataSource.getComponentList(dashboardInt)
        return result.body()?.components ?: emptyList()
    }
}