package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.ComponentDto
import edu.pwr.iotmobile.androidimcs.data.dto.ComponentListDto

interface ComponentRepository {
    suspend fun updateComponentList(componentListDto: ComponentListDto): Result<Unit>
    suspend fun getComponentList(dashboardInt: Int): List<ComponentDto>
}